package com.example.notememodemo.util

import android.app.Activity
import android.content.Intent
import com.example.notememodemo.view.MemoInfoActivity
import com.example.notememodemo.view.AddMemoActivity

object Caller {

    const val EXTRA_MEMO_ID = "note.memo:memo_id"

    private fun createIntent(activity: Activity, cls: Class<*>): Intent {
        val intent = Intent(activity, cls)
//        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return intent
    }

    fun callNewMemo(activity: Activity, calledFrom: Int) {
        val intent = createIntent(activity, AddMemoActivity::class.java)
        activity.startActivity(intent)
    }

    fun callMemoInfo(activity: Activity, id: Int) {
        val intent = createIntent(activity, MemoInfoActivity::class.java)
        intent.putExtra(EXTRA_MEMO_ID, id)
        activity.startActivity(intent)
    }
}