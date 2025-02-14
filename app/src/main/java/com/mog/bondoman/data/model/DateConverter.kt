package com.mog.bondoman.data.model

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun dateFromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}