package com.example.notememodemo.util

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.esafirm.imagepicker.features.ImagePicker
import com.example.notememodemo.R
import com.example.notememodemo.view.MemoInfoActivity
import com.example.notememodemo.view.AddMemoActivity
import java.io.File

object Caller {
    const val EXTRA_MEMO_ID = "note.memo:memo_id"

    const val EXTRA_CALL_NEW_MEMO_CODE = "note.memo:call_new_memo_code"
    const val EXTRA_CALL_MEMO_ID = "note.memo:call_memo_id"

    const val CALL_NEW_MEMO = 2001 //test
    const val CALL_EDIT_MEMO = 2002

    fun callNewMemo(activity: Activity) {
        val intent = Intent(activity, AddMemoActivity::class.java)
        intent.putExtra(EXTRA_CALL_NEW_MEMO_CODE, CALL_NEW_MEMO)
        activity.startActivity(intent)
    }

    fun callEditMemo(activity: Activity, memoId: Int) {
        val intent = Intent(activity, AddMemoActivity::class.java)
        intent.putExtra(EXTRA_CALL_NEW_MEMO_CODE, CALL_EDIT_MEMO)
        intent.putExtra(EXTRA_CALL_MEMO_ID, memoId)
        activity.startActivity(intent)
    }

    fun callMemoInfo(activity: Activity, id: Int) {
        val intent = Intent(activity, MemoInfoActivity::class.java)
        intent.putExtra(EXTRA_MEMO_ID, id)
        activity.startActivity(intent)
    }

    fun callImagePicker(activity: Activity) {
        ImagePicker.create(activity)
            .folderMode(true)
            .toolbarFolderTitle("Folder")
            .toolbarImageTitle("Tap to select")
            .toolbarArrowColor(Color.WHITE)
            .includeVideo(false)
            .multi()
            .limit(20)
            .showCamera(true)
            .imageDirectory("Camera")
//            .theme(R.style.CustomImagePickerTheme)
            .enableLog(false)
            .start()
    }

    fun callCameraAction(activity: Activity, requestCode: Int, photoFile: File) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity.packageManager)?.also {
                val photoURI = FileProvider.getUriForFile(
                    activity,
                    activity.getString(R.string.file_provider_authority),
                    photoFile)

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(intent, requestCode)
            }
        }

    }

}