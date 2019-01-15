package org.ignus.app.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import org.ignus.app.BaseApp
import org.ignus.app.di.scope.PerApplication
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: BaseApp) {
    @Provides
//    @PerApplication
    fun provideApplication(): Application {
        return baseApp
    }

    @Provides
//    @PerApplication
    fun provideApplicationContext(): Context {
        return baseApp.applicationContext
    }
}