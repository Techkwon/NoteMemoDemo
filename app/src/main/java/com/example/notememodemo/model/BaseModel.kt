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
    }
}