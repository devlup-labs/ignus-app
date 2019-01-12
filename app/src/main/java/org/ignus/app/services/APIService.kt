package org.ignus.app.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @POST("token-auth/")
    @Headers("No-Authentication: true")
    fun login(username: String, password: String): Call<ResponseBody>
}