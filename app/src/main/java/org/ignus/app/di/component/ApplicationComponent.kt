package org.ignus.app.di.component

import dagger.Component
import org.ignus.app.BaseApp
import org.ignus.app.di.module.ApplicationModule

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(application: BaseApp)
}