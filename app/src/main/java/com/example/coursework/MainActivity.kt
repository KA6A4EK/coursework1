package com.example.coursework

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.coursework.AppContainer.ContextModule
import com.example.coursework.AppContainer.DaggerAppComponent
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.screens.MainScreen
import com.example.coursework.ui.theme.CourseworkTheme
import com.example.coursework.util.SleepRequestsManager
import com.example.coursework.util.heartRateMeasure.Camera
import com.example.coursework.util.saveStepsInDatabase1
import com.example.coursework.util.showNotificationAlarmManager

class MainActivity : ComponentActivity() {

    lateinit var vm: HealthViewModel
    private val sleepRequestManager by lazy {
        SleepRequestsManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this.deleteDatabase("HealthDatabase")

        val appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
        vm = appComponent.provideHealthViewModel()
        setContent {
            CourseworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val camera = Camera(applicationContext, this)
                    MainScreen(
                        viewModel = vm, context = this,
                        applicationContext = applicationContext,
                        camera = camera
                    )
                }
            }
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.SET_ALARM,
                Manifest.permission.FOREGROUND_SERVICE_HEALTH,
                Manifest.permission.CAMERA
            ),
            1
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "sleepRequestManager.subscribeToSleepUpdates()")
            sleepRequestManager.requestSleepUpdates()
        }
    }
    override fun onStop() {
        super.onStop()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            saveStepsInDatabase1(this)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showNotificationAlarmManager(this, vm)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        sleepRequestManager.unsubscribeFromSleepUpdates()
    }
}