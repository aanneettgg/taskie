package com.example.taskie.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database storing data from entities Task, Diary and Gratefulness.
 */
@Database(
    entities = [Task::class, Diary::class, Gratefulness::class], version = 2)

abstract class TaskieDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun diaryDao(): DiaryDao

    abstract fun gratefulnessDao(): GratefulnessDao

    companion object {
        @Volatile var INSTANCE: TaskieDatabase? = null

        fun getInstance(context: Context): TaskieDatabase {
            kotlin.synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskieDatabase::class.java,
                        "taskie_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}