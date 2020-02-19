package com.example.notememodemo.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.example.notememodemo.R
import com.example.notememodemo.adapter.PhotosAdapter
import com.example.notememodemo.dao.AppDatabase
import com.example.notememodemo.model.Memo
import com.example.notememodemo.repository.MemoRepository
import com.example.notememodemo.viewmodel.MemoViewModel
import com.example.notememodemo.viewmodel.MemoViewModelFactory
import kotlinx.android.synthetic.main.activity_new_memo.*

class AddMemoActivity : AppCompatActivity() {

    private lateinit var adapter: PhotosAdapter

    private lateinit var viewModel: MemoViewModel

    private var photoUris: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_memo)

        initAdapter()
        initViewModel()
        title = "Add Memo"

        setViewClickListener()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_memo_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.btn_save_memo -> {
                saveMemo()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val photos: List<Image> = ImagePicker.getImages(data)

            photoUris = photos.map { it.path }
            photoUris?.let { adapter.addPhotoUris(it) }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setViewClickListener() {
        this.constraint_holder_rv_photos.setOnClickListener { addPhotos() }
    }

    private fun initViewModel() {
        val dao = AppDatabase.getInstance(this).memoDao()
        val repository = MemoRepository.getInstance(dao)
        val viewModelFactory = MemoViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel::class.java)
    }

    private fun initAdapter() {
        adapter = PhotosAdapter(this)
        this.rv_add_photos.adapter = adapter
        this.rv_add_photos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun addPhotos() {
        ImagePicker.create(this)
//            .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
            .folderMode(true) // folder mode (false by default)
            .toolbarFolderTitle("Folder") // folder selection title
            .toolbarImageTitle("Tap to select") // image selection title
            .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
            .includeVideo(false) // Show video on image picker
//            .single() // single mode
            .multi() // multi mode (default mode)
            .limit(20) // max images can be selected (99 by default)
            .showCamera(true) // show camera or not (true by default)
            .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
//            .origin(images) // original selected images, used in multi mode
//            .exclude(images) // exclude anything that in image.getPath()
//            .excludeFiles(files) // same as exclude but using ArrayList<File>
//            .theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
            .enableLog(false) // disabling log
            .start(); // start image picker activity with request code
    }

    private fun saveMemo () {
        if (this.et_title.text.toString().isNotEmpty()) {
            val title = this.et_title.text.toString()
            val memo = this.et_memo_input.toString()
            var photos = Memo.Photos()
            photos.items = photoUris ?: emptyList()

            viewModel.insertMemo(
                Memo(id =0, // auto increment
                    title = title,
                    content = memo,
                    photos = photos)
            )

            finish()
        }
    }
}
