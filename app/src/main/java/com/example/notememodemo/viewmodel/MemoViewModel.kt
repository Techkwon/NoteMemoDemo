package com.example.notememodemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notememodemo.model.Memo
import com.example.notememodemo.repository.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(private val repository: MemoRepository) : ViewModel() {

    internal val memo = MutableLiveData<Memo>()

    internal fun getMemos(): LiveData<List<Memo>> = repository.getMemos()

    internal fun getMemoById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        setMemo(repository.getMemoById(id))
    }

    private fun setMemo(data: Memo) = viewModelScope.launch(Dispatchers.Main) {
        memo.value = data
    }

    internal fun insertMemo(memo: Memo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMemo(memo)
    }
}