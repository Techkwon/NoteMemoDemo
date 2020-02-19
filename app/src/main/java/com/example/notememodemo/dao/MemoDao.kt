package com.example.notememodemo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.notememodemo.model.Memo

@Dao
interface MemoDao {

    @Query("SELECT * FROM Memo ORDER BY ID")
    fun getMemos(): LiveData<List<Memo>>

    @Query("SELECT * FROM Memo WHERE id = :id")
    suspend fun getMemoById(id: Int): Memo

    @Insert(onConflict = REPLACE)
    fun insertMemo(memo: Memo)
}