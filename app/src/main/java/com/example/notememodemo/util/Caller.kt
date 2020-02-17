package com.example.notememodemo.util

import android.app.Activity
import android.content.Intent
import com.example.notememodemo.NewMemoActivity

object Caller {

    private fun createIntent(activity: Activity, cls: Class<*>): Intent {
        val intent = Intent(activity, cls)
//        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return intent
    }

    fun callNewMemo(activity: Activity, calledFrom: Int) {
        val intent = createIntent(activity, NewMemoActivity::class.java)
        activity.startActivity(intent)
    }
}