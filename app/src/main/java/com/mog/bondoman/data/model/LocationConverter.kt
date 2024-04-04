package com.mog.bondoman.data.model

import android.location.Location
import androidx.room.TypeConverter
import com.google.gson.Gson

class LocationConverter {
    @TypeConverter
    fun locationFromString(locationString: String?): Location? {
        return try {
            Gson().fromJson(locationString, Location::class.java)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun locationToString(location: Location?): String? {
        return Gson().toJson(location)
    }
}