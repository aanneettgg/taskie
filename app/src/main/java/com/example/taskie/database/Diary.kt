package com.example.taskie.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An entity defining diary notes.
 */
@Entity(tableName = "diary_table")
data class Diary (
    @ColumnInfo(name = "diary_info")
    var diaryInfo: String,

    @ColumnInfo(name = "diary_date")
    val diaryDate: Long = System.currentTimeMillis()
) {
        @PrimaryKey(autoGenerate = true)
        var diaryId: Long = 0;
}