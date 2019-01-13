package org.ignus.app.di.component

import dagger.Component
import org.ignus.app.BaseApp
import org.ignus.app.di.modules.ApplicationModule
import org.ignus.app.di.scope.PerApplication

@Component(modules = [ApplicationModule::class])
//@PerApplication
interface ApplicationComponent {
    fun inject(application: BaseApp)
}