package com.example.notememodemo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notememodemo.R
import com.example.notememodemo.adapter.MemoPreviewAdapter
import com.example.notememodemo.dao.AppDatabase
import com.example.notememodemo.repository.MemoRepository
import com.example.notememodemo.util.Caller
import com.example.notememodemo.viewmodel.MemoViewModel
import com.example.notememodemo.viewmodel.MemoViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MemoViewModel

    private lateinit var adapter: MemoPreviewAdapter

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewClickListener()
        initViewModel()
        initAdapter()
        getMemos()
    }

    private fun setActionBar() {

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
        })
    }

    private fun initAdapter() {
        adapter = MemoPreviewAdapter(this)
        this.rv_memo_list.layoutManager = LinearLayoutManager(this)
        this.rv_memo_list.adapter = adapter
    }

    private fun setViewClickListener() {
        this.fab_add_memo.setOnClickListener { Caller.callNewMemo(this, 0) }
    }
}
