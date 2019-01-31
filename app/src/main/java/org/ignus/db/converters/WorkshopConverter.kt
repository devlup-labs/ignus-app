package org.ignus.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.ignus.db.models.Location
import org.ignus.db.models.Organiser

class WorkshopConverter {
    @TypeConverter
    fun fromOrganiserListString(value: String): List<Organiser> {
        val objectType = object : TypeToken<List<Organiser>>() {}.type
        return Gson().fromJson(value, objectType)
    }

    @TypeConverter
    fun fromOrganiser(list: List<Organiser>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromLocationString(value: String): Location {
        val objectType = object : TypeToken<Location>() {}.type
        return Gson().fromJson(value, objectType)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        val gson = Gson()
        return gson.toJson(location)
    }
}