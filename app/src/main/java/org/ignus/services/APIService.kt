package org.ignus.services

import io.reactivex.Observable
import org.ignus.db.models.EventCategory
import org.ignus.db.models.EventDetails
import org.ignus.db.models.Sponsor
import org.ignus.db.models.Workshop
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET("event/category-list/")
    fun getEventCategories(): Observable<List<EventCategory>>

    @GET("event/{eventId}/detail/")
    fun getEventDetails(@Path("eventId") eventId: String): Observable<EventDetails>

    @GET("workshops/workshop-list/")
    fun getWorkshopList(): Observable<List<Workshop>>

    @GET("sponsors/sponsors-list/")
    fun getSponsorsList(): Observable<List<Sponsor>>
}