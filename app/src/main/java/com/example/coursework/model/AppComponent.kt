package com.example.coursework.model

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coursework.MainActivity
import com.example.coursework.ViewM.HealthViewModel
import com.example.coursework.ViewM.ProvideViewModel
import dagger.Component
import dagger.Module
import dagger.Provides


@Component(modules = [DaoModule::class, ProvideViewModel::class, RepositoryModule::class,ContextModule::class])
interface AppComponent {

    fun provideContext() : Context
    fun provideHealthViewModel(): HealthViewModel

    fun provideDao(): dao

    fun provideTrainingDao(): TrainingDao

    fun provideHealthRepository(): repository

    fun inject(mainActivity: MainActivity)

}

@Module
class ContextModule(private val context: Context) {
    @Provides
    fun provideContext(): Context {
        return context
    }
}