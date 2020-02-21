package com.example.notememodemo.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notememodemo.R
import com.example.notememodemo.adapter.PhotosAdapter
import com.example.notememodemo.dao.AppDatabase
import com.example.notememodemo.model.Memo
import com.example.notememodemo.repository.MemoRepository
import com.example.notememodemo.util.Caller
import com.example.notememodemo.util.Caller.EXTRA_MEMO_ID
import com.example.notememodemo.util.CommonUtils.showToastMessage
import com.example.notememodemo.viewmodel.MemoViewModel
import com.example.notememodemo.viewmodel.MemoViewModelFactory
import kotlinx.android.synthetic.main.activity_memo_info.*

class MemoInfoActivity : AppCompatActivity() {

    private var memoId: Int = 0

    private lateinit var currentMemo: Memo

    private lateinit var viewModel: MemoViewModel

    private lateinit var adapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_info)
        getIntentData()
        initAdapter()
        initViewModel()
        setMemo()
    }

    override fun onResume() {
        super.onResume()
        getMemo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_memo_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.btn_more -> {
                showPopup(findViewById(R.id.btn_more))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getIntentData() {
        memoId = intent.getIntExtra(EXTRA_MEMO_ID, 0)
    }

    private fun getMemo() {
        viewModel.getMemoById(memoId)
    }

    private fun setMemo() {
        viewModel.memo.observe(this, Observer { memo ->
            setViews(memo)
            currentMemo = memo
        })
    }

    private fun setViews(memo: Memo) {
        title = getString(R.string.memo_info_title)

        val photos = memo.photos.items
        this.tv_memo_title.movementMethod = ScrollingMovementMethod()

        this.tv_memo_title.text = memo.title
        this.tv_memo_content.text = memo.content

        if (photos.isNotEmpty()) {
            this.rv_photos.visibility = View.VISIBLE
            adapter.addPhotoUris(photos)
        } else {
            this.rv_photos.visibility = View.GONE
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showPopup(view: View) {
        val menuBuilder = MenuBuilder(this)
        menuInflater.inflate(R.menu.menu_memo_more, menuBuilder)

        val optionsMenu = MenuPopupHelper(this, menuBuilder, view)
        optionsMenu.setForceShowIcon(true)

        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuModeChange(menu: MenuBuilder?) {}

            override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.menu_edit_memo -> {
                        Caller.callEditMemo(this@MemoInfoActivity, memoId)
                        true
                    }
                    R.id.menu_remove_memo -> {
                        showDialogConfirmRemoval()
                        true
                    }
                    else -> false
                }
            }
        })

        optionsMenu.show()
    }

    private fun showDialogConfirmRemoval() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.title_delete_memo))
            .setMessage(getString(R.string.sure_to_delete_memo))
            .setCancelable(true)
            .setNegativeButton(getString(R.string.cancel)) { view, _ ->
                view.cancel()
            }.setPositiveButton(getString(R.string.delete)) { view, _ ->
                viewModel.deleteMemo(currentMemo)
                view.cancel()
                showToastMessage(this, getString(R.string.memo_is_deleted))
                finish()
            }.create()
        dialog.show()
    }

    private fun initViewModel() {
        val dao = AppDatabase.getInstance(this).memoDao()
        val repository = MemoRepository.getInstance(dao)
        val viewModelFactory = MemoViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel::class.java)
    }

    private fun initAdapter() {
        adapter = PhotosAdapter(this)
        this.rv_photos.adapter = adapter
        this.rv_photos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}
