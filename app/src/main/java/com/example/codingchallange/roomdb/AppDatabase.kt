package com.example.codingchallange.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.codingchallange.roomdb.dao.HighScoreDao
import com.example.codingchallange.roomdb.dao.QuizDao
import com.example.codingchallange.roomdb.entity.HighScoreEntity
import com.example.codingchallange.roomdb.entity.QuizEntity

@Database(
    entities = [QuizEntity::class,
        HighScoreEntity::class],
    version = 2
)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun scoreDao(): HighScoreDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "characters")
                .fallbackToDestructiveMigration()
                .build()
    }

}