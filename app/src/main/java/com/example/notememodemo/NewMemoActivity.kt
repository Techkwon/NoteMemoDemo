package com.example.notememodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.notememodemo.dao.AppDatabase
import com.example.notememodemo.data.Memo
import com.example.notememodemo.repository.MemoRepository
import com.example.notememodemo.viewmodel.MemoViewModel
import com.example.notememodemo.viewmodel.MemoViewModelFactory
import kotlinx.android.synthetic.main.activity_new_memo.*

class NewMemoActivity : AppCompatActivity() {

    private lateinit var viewModel: MemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_memo)

        val dao = AppDatabase.getInstance(this).memoDao()
        val repository = MemoRepository.getInstance(dao)
        val viewModelFactory = MemoViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel::class.java)
        title = "Add Memo"

        setViewClickListener()
    }

    private fun setViewClickListener() {
        this.btn_ok.setOnClickListener {
            if (this.et_title.text.toString().isNotEmpty()) {
                val title = this.et_title.text.toString()
                viewModel.insertMemo(Memo(0, title))
            }
        }
    }
}
