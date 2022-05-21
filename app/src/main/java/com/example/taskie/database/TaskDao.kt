package com.example.taskie.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data access object for handling data from Task entity.
 */
@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * from task_table WHERE strftime(\"%d-%m-%Y\", task_date/1000, 'unixepoch') = :key")
    suspend fun getTasksByDate(key: String): List<Task>

    @Query("SELECT * from task_table WHERE taskId = :key")
    suspend fun getTaskById(key: Long): Task?

    @Delete
    suspend fun delete(task: Task)
}