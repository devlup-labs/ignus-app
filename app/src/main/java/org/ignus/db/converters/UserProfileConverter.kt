package org.ignus.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import org.ignus.db.models.User

class UserProfileConverter {

    @TypeConverter
    fun fromString(value: String): User {
        return Gson().fromJson(value, User::class.java)
    }

    @TypeConverter
    fun fromUser(events: User): String {
        val gson = Gson()
        return gson.toJson(events)
    }
}