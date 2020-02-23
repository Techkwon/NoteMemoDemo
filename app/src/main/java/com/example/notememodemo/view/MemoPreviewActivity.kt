package com.example.notememodemo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notememodemo.R
import com.example.notememodemo.adapter.MemoPreviewAdapter
import com.example.notememodemo.dao.AppDatabase
import com.example.notememodemo.repository.MemoRepository
import com.example.notememodemo.util.Caller
import com.example.notememodemo.viewmodel.MemoViewModel
import com.example.notememodemo.viewmodel.MemoViewModelFactory
import kotlinx.android.synthetic.main.activity_memo_preview.*

class MemoPreviewActivity : AppCompatActivity() {

    private lateinit var viewModel: MemoViewModel

    private lateinit var adapter: MemoPreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_preview)
        setViewClickListener()
        initViewModel()
        initAdapter()
        getMemos()
    }

    private fun initViewModel() {
        val dao = AppDatabase.getInstance(this).memoDao()
        val repository = MemoRepository.getInstance(dao)
        val viewModelFactory = MemoViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel::class.java)
    }

    private fun getMemos() {
        viewModel.getMemos().observe(this, Observer {
            adapter.addMemos(it)

            if (it.isEmpty()) {
                this.tv_empty_memo.visibility = View.VISIBLE
                this.rv_memo_list.visibility = View.GONE
            } else {
                this.tv_empty_memo.visibility = View.GONE
                this.rv_memo_list.visibility = View.VISIBLE
            }
        })
    }

    private fun initAdapter() {
        adapter = MemoPreviewAdapter(this)
        this.rv_memo_list.layoutManager = LinearLayoutManager(this)
        this.rv_memo_list.adapter = adapter

        val divider = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
        getDrawable(R.drawable.list_divider)?.let { divider.setDrawable(it) }
        this.rv_memo_list.addItemDecoration(divider)
    }

    private fun setViewClickListener() {
        this.fab_add_memo.setOnClickListener { Caller.callNewMemo(this) }
    }
}
