package org.ignus.app.di.modules.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.ignus.app.config.BASE_URL
import org.ignus.app.di.scope.PerApplication
import org.ignus.app.services.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [OkHttpClientModule::class])
class APIServiceModule {
    @Provides
    @PerApplication
    fun provideAPIService(okHttpClient: OkHttpClient): APIService {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build().create(APIService::class.java)
    }
}