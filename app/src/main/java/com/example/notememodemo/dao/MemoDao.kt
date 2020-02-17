package com.example.notememodemo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notememodemo.data.Memo

@Dao
interface MemoDao {

    @Query("SELECT * FROM Memo")
    fun getMemos(): LiveData<List<Memo>>

    @Insert
    fun insertMemo(memo: Memo)
}