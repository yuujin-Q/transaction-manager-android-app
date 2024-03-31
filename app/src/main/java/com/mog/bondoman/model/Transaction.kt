package com.mog.bondoman.model

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val category: String,
    val nominal: Double,
    val locationName: String,
    val location: Location,
    val date: Date
)