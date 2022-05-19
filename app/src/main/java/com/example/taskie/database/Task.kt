package com.example.taskie.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An entity defining tasks.
 */
@Entity(tableName = "task_table")
data class Task(
    @ColumnInfo(name = "task_name")
    var taskName: String,

    @ColumnInfo(name = "task_date")
    val taskDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "task_type")
    var taskType: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L
}