package com.example.notememodemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notememodemo.repository.MemoRepository

class MemoViewModelFactory(private val memoRepository: MemoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemoViewModel(memoRepository) as T
    }
}