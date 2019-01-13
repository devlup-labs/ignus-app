package org.ignus.app.di.modules.network

import dagger.Module
import dagger.Provides
import org.ignus.app.di.scope.PerApplication
import org.ignus.app.services.APIServiceInterceptor

@Module
class APIServiceInterceptorModule {
    @Provides
//    @PerApplication
    fun provideAPIServiceInterceptor(): APIServiceInterceptor {
        return APIServiceInterceptor()
    }
}