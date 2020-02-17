package com.example.notememodemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notememodemo.adapter.MemoAdapter
import com.example.notememodemo.dao.AppDatabase
import com.example.notememodemo.repository.MemoRepository
import com.example.notememodemo.util.Caller
import com.example.notememodemo.viewmodel.MemoViewModel
import com.example.notememodemo.viewmodel.MemoViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MemoViewModel

    private lateinit var adapter: MemoAdapter

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dao = AppDatabase.getInstance(this).memoDao()
        val repository = MemoRepository.getInstance(dao)
        val viewModelFactory = MemoViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel::class.java)

        initAdapter()
        setViewClickListener()

        viewModel.getMemo().observe(this, Observer {
            adapter.addMemos(it)
        })
    }

    private fun setActionBar() {

    }

    private fun initAdapter() {
        adapter = MemoAdapter()
        this.rv_memo_list.layoutManager = LinearLayoutManager(this)
        this.rv_memo_list.adapter = adapter
    }

    private fun setViewClickListener() {
        this.fab_add_memo.setOnClickListener { Caller.callNewMemo(this, 0) }
    }

    private fun start() {
        val intent = Intent(this, NewMemoActivity::class.java)
        startActivity(intent)
    }

}
