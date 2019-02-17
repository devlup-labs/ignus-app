package org.ignus.db.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import org.ignus.config.Map
import java.io.Serializable

@Keep
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

@Keep
data class Event(
    val cover_url: String?,
    val start_time: String?,
    val end_time: String?,
    val location: Location?,
    val max_team_size: Int?,
    val min_team_size: Int?,
    val name: String?,
    val organiser_list: List<Organiser>?,
    val unique_id: String?
) : Serializable

@Keep
data class Location(
    val name: String?,
    val latitude: String?,
    val longitude: String?
) : Serializable

@Keep
data class Organiser(
    val avatar_url: String?,
    val email: String?,
    val name: String?,
    val phone: String?
) : Serializable


@Keep
@Entity
data class EventDetails(
    @PrimaryKey
    var id: String,
    val about: String?,
    val details: String?,
    val pdf: String?,
    val url: String?
) : Serializable


@Keep
@Entity
data class Workshop(
    @PrimaryKey
    val id: String,
    val type: String?,
    val about: String?,
    val details: String?,
    val cover: String?,
    val start_time: String?,
    val end_time: String?,
    val location: Location?,
    val name: String?,
    val organiser_list: List<Organiser>?,
    val pdf: String?
) : Serializable

@Keep
data class Sponsor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val link: String?,
    val logo: String?,
    val name: String?,
    val sponsor_type_name: String?,
    val sponsor_type_order: Int?
)

@Keep
data class Venue(
    val name: String,
    val snippet: String,
    val location: LatLng,
    val type: Map = Map.UNFILTERED
)

@Keep
@Entity
data class UserProfile(
    val accommodation_required: Boolean?,
    val address: String?,
    val college: String?,
    val current_year: String?,
    val gender: String?,
    val id: Int?,
    val id_issued: Boolean?,
    val phone: String?,
    val referred_by: String?,
    val state: String?,
    val user: User?,
    @PrimaryKey
    val uuid: String
)

@Keep
data class User(
    val email: String?,
    val first_name: String?,
    val last_name: String?,
    val username: String?
)

@Keep
data class TeamEvents(
    val event: Event,
    val id: Int,
    val leader: Leader,
    val members: List<Member>
)

@Keep
data class Member(
    val user: User
)

@Keep
data class Leader(
    val user: User
)

@Keep
data class Developer(
    val name: String?,
    val github: String?,
    val email: String?,
    val designation: String?
)

@Keep
@Entity
data class Message(
    val message: String?,
    val username: String?,
    @PrimaryKey
    val timestamp: Long?,
    val key: String?
)