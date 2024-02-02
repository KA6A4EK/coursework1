package com.example.coursework.model

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coursework.MainActivity
import com.example.coursework.ViewM.HealthViewModel
import com.example.coursework.ViewM.ProvideViewModel
import dagger.Component


@Component(modules = [DaoModule::class, ProvideViewModel::class, RepositoryModule::class])
interface AppComponent {

    fun provideHealthViewModel(): HealthViewModel

    fun provideDao(): dao

    fun provideTrainingDao(): TrainingDao

    fun provideHealthRepository(): repository

    fun inject(mainActivity: MainActivity)

}

