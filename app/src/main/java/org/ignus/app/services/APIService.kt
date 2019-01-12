package org.ignus.app.services

import org.ignus.app.db.models.EventCategory
import org.ignus.app.db.models.LoginResponse
import org.ignus.app.db.utils.LoginCredentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @POST("token-auth/")
    @Headers("No-Authentication: true")
    fun login(@Body loginCredentials: LoginCredentials): Call<LoginResponse>

    @GET("event/category-list/")
    @Headers("No-Authentication: true")
    fun getEventCategories(): Call<List<EventCategory>>
}