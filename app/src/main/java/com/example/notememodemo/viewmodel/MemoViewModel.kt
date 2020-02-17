package com.example.notememodemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notememodemo.data.Memo
import com.example.notememodemo.repository.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(private val repository: MemoRepository) : ViewModel() {

    internal fun getMemo(): LiveData<List<Memo>> = repository.getMemos()

    internal fun insertMemo(memo: Memo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMemo(memo)
    }

}