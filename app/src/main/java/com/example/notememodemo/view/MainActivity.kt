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
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MemoViewModel

    private lateinit var adapter: MemoPreviewAdapter

    private val testUrl = "http://imgnedews.naver.net/image/144/2019/10/31/0000639701_001_201910310853551193.jpg"

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewClickListener()
        initViewModel()
        initAdapter()
        getMemos()

//        GlobalScope.launch {
//            check()
//        }

    }

    private suspend fun check() {

        withContext(Dispatchers.IO) {
            val url = URL("http://imgnedews.naver.net/image/144/2019/10/31/0000639701_001_201910310853551193.jpg")
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
//            val responseCode = connection.responseCode
//            println("woogear check=$responseCode")

        }

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
