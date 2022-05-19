package com.example.taskie.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An entity defining grateful notes.
 */
@Entity(tableName="gratefulness_table")
data class Gratefulness(
    @ColumnInfo(name = "gratefulness_info")
    var gratefulnessInfo: String,

    @ColumnInfo(name = "gratefulness_date")
    val gratefulnessDate: Long = System.currentTimeMillis(),
) {
    @PrimaryKey(autoGenerate = true)
    var gratefulnessId: Long = 0L
}