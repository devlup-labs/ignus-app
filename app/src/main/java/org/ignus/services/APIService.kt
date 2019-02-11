package org.ignus.services

import com.google.gson.JsonObject
import io.reactivex.Observable
import org.ignus.db.models.*
import retrofit2.http.*

interface APIService {

    @GET("event/category-list/")
    fun getEventCategories(): Observable<List<EventCategory>>

    @GET("event/{eventId}/detail/")
    fun getEventDetails(@Path("eventId") eventId: String): Observable<EventDetails>

    @GET("workshops/workshop-list/")
    fun getWorkshopList(): Observable<List<Workshop>>

    @FormUrlEncoded
    @POST("token-auth/")
    fun getUserToken(@Field("username") username: String, @Field("password") password: String): Observable<JsonObject>

    @GET("accounts/user-profile/current/")
    fun getUserProfile(@Header("Authorization") token: String): Observable<UserProfile>

    @GET("accounts/user-profile/current/solo-event-registrations/")
    fun getSoloRegisteredEvents(@Header("Authorization") token: String): Observable<List<Event>>

    @GET("accounts/user-profile/current/team-event-registrations/")
    fun getTeamRegisteredEvents(@Header("Authorization") token: String): Observable<List<TeamEvents>>

    @GET("accounts/user-profile/current/workshop-registrations/")
    fun getRegisteredWorkshops(@Header("Authorization") token: String): Observable<List<Workshop>>
}