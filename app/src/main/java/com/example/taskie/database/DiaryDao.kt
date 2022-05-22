package com.example.taskie.database

import androidx.room.*

/**
 * Data access object for handling data from Diary entity.
 */
@Dao
interface DiaryDao {

    @Insert
    suspend fun insert(vararg diary: Diary)

    @Update
    suspend fun update(diary: Diary)

    @Query("SELECT * from diary_table WHERE strftime(\"%d-%m-%Y\", diary_date/1000, 'unixepoch') = :key")
    suspend fun getDiaryByDate(key: String): Diary?

}