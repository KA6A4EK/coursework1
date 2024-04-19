package com.example.coursework.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class OnRestartBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            saveStepsInDatabase(context)
        }
    }

}