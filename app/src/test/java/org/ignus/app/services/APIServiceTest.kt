package org.ignus.app.services

import org.junit.Assert.*
import okhttp3.OkHttpClient
import org.ignus.app.config.BASE_URL
import org.ignus.app.db.utils.LoginCredentials
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIServiceTest {
    companion object {
        private val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(APIServiceInterceptor())
                .build()
        private val service = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build().create(APIService::class.java)
    }

    @Test
    fun testLogin() {
        val call = service.login(LoginCredentials("random", "random"))
        val response = call.execute()
        if (!response.isSuccessful)
            assertTrue(response.errorBody()!!
                    .string()
                    .contains("Unable to log in with provided credentials."))
    }
}