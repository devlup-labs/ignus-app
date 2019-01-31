package org.ignus.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class EventCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String?,
    val about: String?,
    val cover: String?,
    val events: List<Event>?,
    val parent_type: String?
) : Serializable

data class Event(
    val cover_url: String?,
    val date_time: String?,
    val location: Location?,
    val max_team_size: Int?,
    val min_team_size: Int?,
    val name: String?,
    val organiser_list: List<Organiser>?,
    val unique_id: String?
) : Serializable

data class Location(
    val name: String?,
    val latitude: String?,
    val longitude: String?
) : Serializable

data class Organiser(
    val avatar_url: String?,
    val email: String?,
    val name: String?,
    val phone: String?
) : Serializable


@Entity
data class EventDetails(
    @PrimaryKey
    var id: String,
    val about: String?,
    val details: String?,
    val pdf: String?,
    val url: String?
) : Serializable


@Entity
data class Workshop(
    @PrimaryKey
    val unique_id: String,
    val about: String?,
    val cover: String?,
    val custom_html_lower: String?,
    val custom_html_upper: String?,
    val start_time: String?,
    val end_time: String?,
    val details: String?,
    val location: Location?,
    val name: String?,
    val organiser_list: List<Organiser>?,
    val pdf: String?,
    val slug: String?,
    val type: String?
) : Serializable

data class Sponsor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val link: String?,
    val logo: String?,
    val name: String?,
    val sponsor_type_name: String?,
    val sponsor_type_order: Int?
)