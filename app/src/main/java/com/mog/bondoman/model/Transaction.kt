package com.mog.bondoman.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var ownerId: Long = -1,
    var title: String,
    var category: String,
    var nominal: Double,
    var location: String,
    var date: Date
)