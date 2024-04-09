package com.example.coursework.AppContainer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.coursework.MainActivity
import com.example.coursework.data.DaoModule
import com.example.coursework.data.dao
import com.example.coursework.data.local.HealthManager
import com.example.coursework.domain.repository.RepositoryModule
import com.example.coursework.domain.repository.repository
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.ViewM.ProvideViewModel
import dagger.Component
import dagger.Module
import dagger.Provides


@Component(modules = [DaoModule::class, DataStoreModule::class, ProvideViewModel::class, RepositoryModule::class, ContextModule::class,HealthManager::class])
interface AppComponent {
    fun provideContext(): Context
    fun provideHealthViewModel(): HealthViewModel

    fun provideDao(): dao

    fun provideHealthManager(): HealthManager
    fun provideDataStore():  DataStore<Preferences>
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

@Module
class DataStoreModule() {
    @Provides
    fun provideDataStore(
        context: Context,
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("healthDatastorePreferences")
        }
    }
}

