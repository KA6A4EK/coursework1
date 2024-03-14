package com.example.coursework.AppContainer

import android.content.Context
import com.example.coursework.MainActivity
import com.example.coursework.data.DaoModule
import com.example.coursework.data.dao
import com.example.coursework.domain.repository.RepositoryModule
import com.example.coursework.domain.repository.repository
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.ViewM.ProvideViewModel
import dagger.Component
import dagger.Module
import dagger.Provides


@Component(modules = [DaoModule::class, ProvideViewModel::class, RepositoryModule::class, ContextModule::class])
interface AppComponent {
    fun provideContext() : Context
    fun provideHealthViewModel(): HealthViewModel

    fun provideDao(): dao


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