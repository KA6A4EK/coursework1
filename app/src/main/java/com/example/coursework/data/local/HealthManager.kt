package com.example.coursework.data.local


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.coursework.domain.model.User
import com.example.coursework.presentation.screens.getCurrentDay
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Module
class HealthManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val CountWaterKey = stringPreferencesKey("CountWaterKey")
    private val LastHourStepsKey = intPreferencesKey("LastHourStepsKey")
    private val UserKey = stringPreferencesKey("UserKey")
    suspend fun readUser(): User {
        val data = dataStore.data.firstOrNull()?.get(UserKey)
        if (data != null) {
            return Gson().fromJson(data, User::class.java)
        } else {
            return User()
        }
    }

    suspend fun saveUser(value: User) {
        dataStore.edit { preferences ->
            preferences[UserKey] = Gson().toJson(value)
        }
    }

    suspend fun readCountWater(): String {
        val data = dataStore.data.firstOrNull()?.get(CountWaterKey)
        saveCountWater(0)
        return data ?: "${getCurrentDay()} 0"
    }

    suspend fun saveCountWater(value: Int) {
        dataStore.edit { preferences ->
            preferences[CountWaterKey] = "${getCurrentDay()} $value"
        }
    }

    suspend fun readLastHourSteps(): Int? {
        val data = dataStore.data.firstOrNull()?.get(LastHourStepsKey)
        return data
    }

    suspend fun saveLastHourSteps(value: Int) {
        dataStore.edit { preferences ->
            preferences[LastHourStepsKey] = value
        }
    }

}

@Module
class ProvideHealthManager() {
    @Provides
    @Singleton
    fun provideHealthManager(
        dataStore: DataStore<Preferences>,
    ): HealthManager = HealthManager(dataStore)

}



