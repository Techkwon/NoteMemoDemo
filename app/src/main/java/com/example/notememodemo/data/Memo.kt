package com.example.notememodemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Memo")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String
)