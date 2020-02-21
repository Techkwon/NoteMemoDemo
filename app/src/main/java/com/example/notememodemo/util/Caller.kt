package com.example.notememodemo.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.notememodemo.R
import com.example.notememodemo.view.MemoInfoActivity
import com.example.notememodemo.view.AddMemoActivity

object Caller {

    const val EXTRA_MEMO_ID = "note.memo:memo_id"
    const val EXTRA_CALL_NEW_MEMO_CODE = "note.memo:call_new_memo_code"
    const val EXTRA_CALL_MEMO_ID = "note.memo:call_memo_id"

    const val CALL_NEW_MEMO = 2001
    const val CALL_EDIT_MEMO = 2002

    private fun createIntent(activity: Activity, cls: Class<*>): Intent {
        val intent = Intent(activity, cls)
//        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return intent
    }

    fun callNewMemo(activity: Activity, calledFrom: Int) {
        val intent = createIntent(activity, AddMemoActivity::class.java)
        intent.putExtra(EXTRA_CALL_NEW_MEMO_CODE, CALL_NEW_MEMO)
        activity.startActivity(intent)
    }

    fun callEditMemo(activity: Activity, memoId: Int) {
        val intent = createIntent(activity, AddMemoActivity::class.java)
        intent.putExtra(EXTRA_CALL_NEW_MEMO_CODE, CALL_EDIT_MEMO)
        intent.putExtra(EXTRA_CALL_MEMO_ID, memoId)
        activity.startActivity(intent)
    }

    fun callMemoInfo(activity: Activity, id: Int) {
        val intent = createIntent(activity, MemoInfoActivity::class.java)
        intent.putExtra(EXTRA_MEMO_ID, id)
        activity.startActivity(intent)
    }

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
}