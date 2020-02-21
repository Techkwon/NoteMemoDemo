package com.example.notememodemo.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.notememodemo.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    fun displayImage(context: Context, view: AppCompatImageView, path: String) {
        Glide.with(context)
            .load(path)
            .override(300, 200)
            .error(R.drawable.ic_placeholder_error)
            .centerCrop()
            .into(view)
    }

    fun showToastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SimpleDateFormat")
    fun createPhotoFile(context: Context): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }
}