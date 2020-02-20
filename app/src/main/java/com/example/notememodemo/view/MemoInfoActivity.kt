package com.example.notememodemo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notememodemo.R
import com.example.notememodemo.adapter.PhotosAdapter
import com.example.notememodemo.dao.AppDatabase
import com.example.notememodemo.repository.MemoRepository
import com.example.notememodemo.util.Caller.EXTRA_MEMO_ID
import com.example.notememodemo.viewmodel.MemoViewModel
import com.example.notememodemo.viewmodel.MemoViewModelFactory
import kotlinx.android.synthetic.main.activity_memo_info.*

class MemoInfoActivity : AppCompatActivity() {

    private var memoId: Int = 0

    private lateinit var viewModel: MemoViewModel

    private lateinit var adapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_info)

        memoId = intent.getIntExtra(EXTRA_MEMO_ID, 0)

        initAdapter()
        initViewModel()
        getMemo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getMemo() {
        viewModel.getMemoById(memoId)

        viewModel.memo.observe(this, Observer { memo ->
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
        })
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
