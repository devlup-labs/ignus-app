package org.ignus.app.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class EventCategory(@PrimaryKey @SerializedName("name")
                         val name: String = "",
                         @SerializedName("parent_type")
                         val parentType: String = "",
                         @SerializedName("cover")
                         val cover: String = "",
                         @SerializedName("about")
                         val about: String = "",
                         @SerializedName("events")
                         val events: List<Event>?)