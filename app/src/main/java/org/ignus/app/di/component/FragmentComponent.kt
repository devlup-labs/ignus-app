package org.ignus.app.di.component

import dagger.Component
import org.ignus.app.di.modules.FragmentModule
import org.ignus.app.di.scope.PerActivity
import org.ignus.app.services.APIService
import org.ignus.app.ui.categories.EventCategoriesFragment

@Component(modules = [FragmentModule::class], dependencies = [APIServiceComponent::class])
//@PerActivity
interface FragmentComponent {
    fun inject(eventCategoriesFragment: EventCategoriesFragment)
}