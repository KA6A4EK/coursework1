package com.example.coursework.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.coursework.domain.model.Day
import com.example.coursework.domain.model.HeartRateConverter
import com.example.coursework.domain.model.StepsAtTheDayConverter
import com.example.coursework.domain.model.TargetsConverter
import com.example.coursework.domain.model.TrainingListConverter
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




@TypeConverters(TargetsConverter::class,TrainingListConverter::class, StepsAtTheDayConverter::class ,HeartRateConverter::class)
@Database(entities = [Day::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val HealthDao: dao
}

@Module
class DaoModule() {
    @Provides
    fun provideDao(database: AppDatabase): dao {
        return database.HealthDao
    }


    @Provides
    fun provideDB(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "HealthDatabase"
        )
//            .fallbackToDestructiveMigration()
            .build()
    }
}




