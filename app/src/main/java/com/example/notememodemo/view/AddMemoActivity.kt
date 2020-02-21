package com.example.notememodemo.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
import com.example.notememodemo.R
import com.example.notememodemo.adapter.AddPhotosAdapter
import com.example.notememodemo.dao.AppDatabase
import com.example.notememodemo.model.Memo
import com.example.notememodemo.repository.MemoRepository
import com.example.notememodemo.util.Caller.CALL_EDIT_MEMO
import com.example.notememodemo.util.Caller.CALL_NEW_MEMO
import com.example.notememodemo.util.Caller.EXTRA_CALL_MEMO_ID
import com.example.notememodemo.util.Caller.EXTRA_CALL_NEW_MEMO_CODE
import com.example.notememodemo.util.Caller.callCameraAction
import com.example.notememodemo.util.Caller.callImagePicker
import com.example.notememodemo.util.CommonUtils
import com.example.notememodemo.util.CommonUtils.createPhotoFile
import com.example.notememodemo.util.CommonUtils.showToastMessage
import com.example.notememodemo.viewmodel.MemoViewModel
import com.example.notememodemo.viewmodel.MemoViewModelFactory
import kotlinx.android.synthetic.main.activity_new_memo.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddMemoActivity : AppCompatActivity() {
    private var callingType = 0

    private var editMemoId = 0

    private lateinit var adapter: AddPhotosAdapter

    private lateinit var viewModel: MemoViewModel

    private var photoUris: List<String>? = null

    private var photoFile: File? = null

    private var originalMemo: String = ""

    companion object {
        private const val REQUEST_CAMERA_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_memo)
        getIntentData()
        initViewModel()
        initAdapter()
        setViews()
        getSelectedPhotos()
        setViewClickListener()
    }

    override fun onBackPressed() {
        checkTextsBeforeExit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save_memo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                checkTextsBeforeExit()
                true
            }

            R.id.btn_save_memo -> {
                saveMemo()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_CODE && grantResults[0] == 0 && grantResults[1] == 0) {
            takePhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val localPaths = ImagePicker.getImages(data).map { it.path }
            viewModel.updateSelectedPhotos(localPaths)
        }

        if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            photoFile?.absolutePath?.let {
                viewModel.updateSelectedPhotos(listOf(it))
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getIntentData() {
        callingType = intent.getIntExtra(EXTRA_CALL_NEW_MEMO_CODE, 0)
        if (callingType == CALL_EDIT_MEMO) editMemoId = intent.getIntExtra(EXTRA_CALL_MEMO_ID, -1)
    }

    private fun setViews() = when (callingType) {
        CALL_NEW_MEMO -> {
            title = getString(R.string.new_memo)
        }
        CALL_EDIT_MEMO -> {
            getMemo()
            title = getString(R.string.edit_memo)
        }
        else -> {
            showInvalidRequest()
        }
    }

    private fun getMemo() {
        viewModel.getMemoById(editMemoId)
        viewModel.memo.observe(this, Observer { memo ->
            setMemoView(memo)
        })
    }

    private fun setMemoView(memo: Memo) {
        this.et_title.setText(memo.title)
        this.et_memo_input.setText(memo.content)
        viewModel.updateSelectedPhotos(memo.photos.items)
        originalMemo = memo.content
    }

    private fun getSelectedPhotos() {
        viewModel.selectedPhotos.observe(this, Observer { images ->
            photoUris = images

            if (images.isEmpty()) {
                this.rv_add_photos.visibility = View.GONE
                this.tv_empty_photos.visibility = View.VISIBLE
            } else {
                adapter.updateList(images)
                this.rv_add_photos.visibility = View.VISIBLE
                this.tv_empty_photos.visibility = View.GONE
            }
        })
    }

    private fun saveMemo () {
        if (this.et_memo_input.text.toString().isNotEmpty()) {
            val title = this.et_title.text.toString()
            val memoContent = this.et_memo_input.text.toString()
            val photos = Memo.Photos()
            photos.items = photoUris ?: emptyList()

            val memo = Memo(
                id = editMemoId, // id = 0, auto increment
                title = if (title.isEmpty()) getString(R.string.no_title) else title,
                content = memoContent,
                photos = photos)


            if (callingType == CALL_NEW_MEMO) viewModel.insertMemo(memo)
            else if (callingType == CALL_EDIT_MEMO) viewModel.updateMemo(memo)

            finish()
        }
    }

    @SuppressLint("InflateParams")
    private fun showAddPhotosDialog() {
        val layout = layoutInflater.inflate(R.layout.dialog_add_gallery_options, null)

        val btnAddFromGallery = layout.findViewById<Button>(R.id.btn_add_from_gallery)
        val btnAddFromCamera = layout.findViewById<Button>(R.id.btn_add_from_camera)
        val btnAddFromUrl = layout.findViewById<Button>(R.id.btn_add_from_url)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setView(layout)
            .create()

        dialog.show()

        btnAddFromGallery.setOnClickListener { callImagePicker(this); dialog.dismiss() }
        btnAddFromCamera.setOnClickListener { takePhoto(); dialog.dismiss() }
        btnAddFromUrl.setOnClickListener { showAddUrlDialog(); dialog.dismiss() }
    }

    private fun takePhoto() {
        if (isCameraPermissionGranted()) makePhotoFile()
        else requestCameraPermission()
    }

    private fun makePhotoFile() {
        try {
            photoFile = createPhotoFile(this)
            photoFile?.let { callCameraAction(this, REQUEST_CAMERA_CODE, it) }
        } catch (e: Exception) {
            showToastMessage(this, "Error: "  + e.message.toString())
        }
    }

    @SuppressLint("InflateParams")
    private fun showAddUrlDialog() {
        val layout = layoutInflater.inflate(R.layout.dialog_add_url, null)
        val etAddUrl = layout.findViewById<AppCompatEditText>(R.id.et_add_photo_url)
        val tvError = layout.findViewById<AppCompatTextView>(R.id.tv_error_message)

        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.input_url))
            .setCancelable(true)
            .setView(layout)
            .setPositiveButton(getString(R.string.confirm)) { view, _ ->
                val url = etAddUrl.text.toString()
                if (isUrlValid(url)) {
                    viewModel.updateSelectedPhotos(listOf(url).toMutableList())
                    view.cancel()
                } else {
                    tvError.text = getString(R.string.invalid_url)
                }
            }
            .create()
        dialog.show()
    }

    private fun isCameraPermissionGranted(): Boolean {
        val cameraPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return cameraPermissionCheck == PackageManager.PERMISSION_GRANTED
                && writePermissionCheck == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CAMERA_CODE
        )
    }

    private fun isUrlValid(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    private fun initViewModel() {
        val dao = AppDatabase.getInstance(this).memoDao()
        val repository = MemoRepository.getInstance(dao)
        val viewModelFactory = MemoViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel::class.java)
    }

    private fun initAdapter() {
        adapter = AddPhotosAdapter(this, viewModel)
        this.rv_add_photos.adapter = adapter
        this.rv_add_photos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setViewClickListener() {
        this.constraint_holder_rv_photos.setOnClickListener { showAddPhotosDialog() }
    }

    private fun checkTextsBeforeExit() {
        if (callingType == CALL_NEW_MEMO) {
            if (this.et_memo_input.text.toString().isNotEmpty()) {
                showWarningBeforeExit(getString(R.string.warning_memo_is_on_wrting))
            } else {
                finish()
            }
        } else if (callingType == CALL_EDIT_MEMO) {
            if (this.et_memo_input.text.toString() != originalMemo) {
                showWarningBeforeExit(getString(R.string.warning_memo_is_edited))
            } else {
                finish()
            }
        }
    }

    private fun showWarningBeforeExit(msg: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(msg)
            .setCancelable(true)
            .setNegativeButton(getString(R.string.cancel)) { view, _ ->
                view.cancel()
            }.setPositiveButton(getString(R.string.confirm)) { view, _ ->
                view.cancel()
                finish()
            }.create()
        dialog.show()
    }

    private fun showInvalidRequest() {
        showToastMessage(this, getString(R.string.wrong_request_message))
        finish()
    }
}
