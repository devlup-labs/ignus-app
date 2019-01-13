package org.ignus.app.di.component

import dagger.Component
import org.ignus.app.di.modules.network.APIServiceModule
import org.ignus.app.di.scope.PerApplication
import org.ignus.app.services.APIService

@Component(modules = [APIServiceModule::class])
//@PerApplication
interface APIServiceComponent {
    fun getAPIService(): APIService
}