package org.ignus.app.db.models

import com.google.gson.annotations.SerializedName

data class EventCategory(@SerializedName("name")
                         val name: String = "",
                         @SerializedName("parent_type")
                         val parentType: String = "",
                         @SerializedName("cover")
                         val cover: String = "",
                         @SerializedName("about")
                         val about: String = "",
                         @SerializedName("events")
                         val events: List<Event>?)