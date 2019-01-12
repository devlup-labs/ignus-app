package org.ignus.app.db.models

import com.google.gson.annotations.SerializedName

data class Event(@SerializedName("name")
                 val name: String = "",
                 @SerializedName("unique_id")
                 val uniqueId: String = "",
                 @SerializedName("cover_url")
                 val coverUrl: String? = "",
                 @SerializedName("min_team_size")
                 val minTeamSize: Int = 0,
                 @SerializedName("max_team_size")
                 val maxTeamSize: Int = 0,
                 @SerializedName("date_time")
                 val dateTime: String = "",
                 @SerializedName("location")
                 val location: Location?,
                 @SerializedName("organiser_list")
                 val organiserList: List<Organiser>?)