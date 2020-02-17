package com.example.notememodemo.repository

import androidx.lifecycle.LiveData
import com.example.notememodemo.dao.MemoDao
import com.example.notememodemo.data.Memo

class MemoRepository
constructor(private val dao: MemoDao) {

    companion object {
        @Volatile
        private var instance: MemoRepository? = null

        fun getInstance(dao: MemoDao) = instance ?: synchronized(this) {
            instance ?: MemoRepository(dao).also { instance = it }
        }
    }

    internal fun getMemos(): LiveData<List<Memo>> {
        return dao.getMemos()
    }

    internal suspend fun insertMemo(memo: Memo) {
        dao.insertMemo(memo)
    }
}