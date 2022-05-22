package com.example.taskie.database

import androidx.room.*

/**
 * Data access object for handling data from Gratefulness entity.
 */
@Dao
interface GratefulnessDao {

    @Insert
    suspend fun insert(gratefulness: Gratefulness)

    @Update
    suspend fun update(gratefulness: Gratefulness)

    @Query("SELECT * from gratefulness_table WHERE strftime(\"%d-%m-%Y\", gratefulness_date/1000, 'unixepoch') = :key")
    suspend fun getGratefulnessByDate(key: String): Gratefulness?

}