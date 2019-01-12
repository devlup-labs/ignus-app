package org.ignus.app.di.modules.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.ignus.app.di.scope.PerApplication
import org.ignus.app.services.APIServiceInterceptor

@Module(includes = [APIServiceInterceptorModule::class])
class OkHttpClientModule {
    @Provides
    @PerApplication
    fun provideOkHttpClient(apiServiceInterceptor: APIServiceInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(apiServiceInterceptor)
                .build()
    }
}