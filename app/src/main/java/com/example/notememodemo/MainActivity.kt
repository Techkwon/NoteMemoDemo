package com.example.notememodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notememodemo.fragment.MemoListFragment
import com.example.notememodemo.viewmodel.MemoViewModel

class MainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager

    private val memoListFragment: MemoListFragment? = null

    private lateinit var viewModel: MemoViewModel

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_info)
    }
}
