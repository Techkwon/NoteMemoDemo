package com.example.notememodemo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.notememodemo.model.Memo

@Dao
interface MemoDao {

    @Query("SELECT * FROM Memo ORDER BY ID DESC")
    fun getMemos(): LiveData<List<Memo>>

    @Query("SELECT * FROM Memo WHERE id = :id")
    suspend fun getMemoById(id: Int): Memo

    @Insert(onConflict = REPLACE)
    fun insertMemo(memo: Memo)

    @Update
    fun updateMemo(memo: Memo)

    @Delete
    fun deleteMemo(memo: Memo)
}