package com.example.coursework.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent
import com.google.android.gms.location.SleepSegmentRequest
import org.chromium.base.Log


class SleepReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e(TAG, "SleepReceiver SleepReceiver SleepReceiver SleepReceiver")

        if (SleepSegmentEvent.hasEvents(intent)) {
            val events =
                SleepSegmentEvent.extractEvents(intent)
            val sh = context.getSharedPreferences("HealthPrefs",Context.MODE_PRIVATE)
            val sleepTime = events.sumOf { it.endTimeMillis - it.startTimeMillis }
            Log.e(TAG, "Logging SleepSegmentEvents")

            for (event in events) {
                Log.e(
                    TAG,
                    "${event.startTimeMillis} to ${event.endTimeMillis} with status ${event.status}"
                )
            }
            sh.edit().putLong("sleepTime" ,sleepTime).apply()//TODO тут надо это или модуля не будет совсем
            Log.e(TAG,"sleepTimesleepTime$sleepTime")
        } else if (SleepClassifyEvent.hasEvents(intent)) {
            val events = SleepClassifyEvent.extractEvents(intent)

            Log.e(TAG, "Logging SleepClassifyEvents")

            for (event in events) {
                Log.e(
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
        Log.e(TAG, "subscribeToSleepUpdates")
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
        Log.e(TAG, "requestSleepUpdates")

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACTIVITY_RECOGNITION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG,"subscribeToSleepUpdates")
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
            Log.e(TAG, "Permission to listen for Sleep Activity has been removed")
        })
    }
    companion object {
        private const val TAG = "SLEEP_RECEIVER"
    }
}