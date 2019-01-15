package org.ignus.app.db.models.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.ignus.app.db.models.Event

class EventListConverter {
    @TypeConverter
    fun fromString(value: String): List<Event> {
        val objectType = object : TypeToken<List<Event>>() {}.type
        return Gson().fromJson(value, objectType)
    }

    @TypeConverter
    fun fromEvents(events: List<Event>): String {
        val gson = Gson()
        return gson.toJson(events)
    }
}