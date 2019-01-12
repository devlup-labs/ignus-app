package org.ignus.app.services

import org.junit.Assert.*
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.ignus.app.mockdata.VALID_JWT_TOKEN
import org.ignus.app.db.utils.LoginCredentials
import org.ignus.app.mockdata.eventCategoryList
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(JUnit4::class)
class APIServiceTest {
    private lateinit var mockServer: MockWebServer
    private lateinit var service: APIService
    private lateinit var okHttpClient: OkHttpClient

    @Before
    @Throws
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.setDispatcher(MockServerDispatcher())
        mockServer.start()
        val baseUrl = mockServer.url("")
        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(APIServiceInterceptor())
                .build()
        service = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build().create(APIService::class.java)
    }

    @Test
    fun testLogin() {
        val response = service.login(LoginCredentials("random", "random")).execute()
        assertEquals(200, response.code())
        assertEquals(VALID_JWT_TOKEN, response.body()?.token)
    }

    @Test
    fun testGetEventCategories() {
        val response = service.getEventCategories().execute()
        assertEquals(200, response.code())
        assertEquals(eventCategoryList, response.body())
    }

    @After
    @Throws
    fun tearDown() {
        mockServer.shutdown()
    }
}