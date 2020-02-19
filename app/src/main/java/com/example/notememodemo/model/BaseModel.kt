package com.example.notememodemo.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder

open class BaseModel {
    companion object {
        private fun gsonBuilder(): GsonBuilder {
            val builder = GsonBuilder()
            builder.serializeNulls()

            return builder
        }

        fun gson(): Gson {
            val builder = gsonBuilder()

            return builder.create()
        }

        fun <T> deepCopy(`object`: T, type: Class<T>): T? {
            return try {
                BaseModel.gson().fromJson(BaseModel.gson().toJson(`object`, type), type)
            } catch (e: Exception) {
                e.printStackTrace()

                null
            }
        }
    }
}