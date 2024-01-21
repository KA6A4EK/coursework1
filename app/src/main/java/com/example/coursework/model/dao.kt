package com.example.coursework.model

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import dagger.Module
import dagger.Provides


@Dao
interface dao {
    @Query("SELECT * FROM Health")
    suspend fun getAllDays(): List<Day>

    @Insert
    suspend fun Insert(day: Day)

    @Update
    suspend fun Update(day: Day)
}


@Dao
interface TrainingDao{
    @Query("SELECT * FROM Training_Activity")
    suspend fun getAllActivities():List<Training>

    @Insert
    suspend fun Insert(training: Training)
}


@Database(entities = [Day::class,Training::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val HealthDao: dao
    abstract val TrainingDao: TrainingDao
}

@Module
class DaoModule(private val applicationContext: Context) {
    @Provides
    fun provideDao(database: AppDatabase): dao {
        return database.HealthDao
    }
    @Provides
    fun provideTrainingDao(database: AppDatabase): TrainingDao {
        return database.TrainingDao
    }

    @Provides
    fun provideDB(): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "HealthDatabase"
        ).build()
    }
}



