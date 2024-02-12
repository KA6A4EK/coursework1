package com.example.coursework.model

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent
import com.google.android.gms.location.SleepSegmentRequest

class SleepReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "SleepReceiver")

        if (SleepSegmentEvent.hasEvents(intent)) {
            val events =
                SleepSegmentEvent.extractEvents(intent)

            Log.d(TAG, "Logging SleepSegmentEvents")

            for (event in events) {
                Log.d(
                    TAG,
                    "${event.startTimeMillis} to ${event.endTimeMillis} with status ${event.status}"
                )
            }
            val sh = context.getSharedPreferences("HealthPrefs",Context.MODE_PRIVATE)
            val sleepTime = events.sumOf { it.endTimeMillis - it.startTimeMillis }
            sh.edit().putLong("sleepTime" ,sleepTime).apply()
            Log.e(TAG,"sleepTimesleepTime$sleepTime")
        } else if (SleepClassifyEvent.hasEvents(intent)) {
            val events = SleepClassifyEvent.extractEvents(intent)

            Log.d(TAG, "Logging SleepClassifyEvents")

            for (event in events) {
                Log.d(
                    TAG,
                    "Confidence: ${event.confidence} - Light: ${event.light} - Motion: ${event.motion}"
                )
            }
        }
    }

    companion object {
        private const val TAG = "SLEEP_RECEIVER"
        fun createPendingIntent(context: Context): PendingIntent {
        Log.e(TAG,"createPendingIntent")
            val intent = Intent(context, SleepReceiver::class.java)
            return PendingIntent.getBroadcast(
                context, 3125, intent, PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}

class SleepRequestsManager(private val context: Context) {
    private val sleepReceiverPendingIntent by lazy {
        SleepReceiver.createPendingIntent(context)
    }

    @SuppressLint("MissingPermission")
    fun subscribeToSleepUpdates() {
        Log.d(TAG, "subscribeToSleepUpdates")

        ActivityRecognition.getClient(context)
            .requestSleepSegmentUpdates(
                sleepReceiverPendingIntent,
                SleepSegmentRequest.getDefaultSleepSegmentRequest()
            )

    }

    fun unsubscribeFromSleepUpdates() {
        ActivityRecognition.getClient(context)
            .removeSleepSegmentUpdates(sleepReceiverPendingIntent)
    }

    fun requestSleepUpdates(requestPermission: () -> Unit = {}) {
        Log.d(TAG, "requestSleepUpdates")

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACTIVITY_RECOGNITION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(ContentValues.TAG,"subscribeToSleepUpdates")
            subscribeToSleepUpdates()
        } else {
            requestPermission()
        }
    }

}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sleepRequestManager = SleepRequestsManager(context)

        sleepRequestManager.requestSleepUpdates(requestPermission = {
            Log.d(TAG, "Permission to listen for Sleep Activity has been removed")
        })
    }
    companion object {
        private const val TAG = "SLEEP_RECEIVER"
    }
}
