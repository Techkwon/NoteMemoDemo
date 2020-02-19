package com.example.notememodemo.model

import androidx.room.*
import com.google.gson.reflect.TypeToken

@Entity(tableName = "Memo")
data class Memo (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    @field:Embedded val photos: Photos) : BaseModel() {

    class Photos {
        @TypeConverters(PhotoItemTypeConverter::class)
        lateinit var items: List<String>
    }

    class PhotoItemTypeConverter {
        @TypeConverter
        fun stringToList(json: String): List<String> {
            val type = object : TypeToken<List<String>>() {}.type
            return gson().fromJson<List<String>>(json, type)
        }

        @TypeConverter
        fun listToString(list: List<String>): String {
            val type = object : TypeToken<List<String>>() {}.type
            return gson().toJson(list, type)
        }
    }
}