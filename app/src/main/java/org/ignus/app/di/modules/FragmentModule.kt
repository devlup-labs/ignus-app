package org.ignus.app.di.modules

import dagger.Module
import dagger.Provides
import org.ignus.app.di.modules.network.APIServiceModule
import org.ignus.app.di.scope.PerActivity
import org.ignus.app.ui.categories.EventCategoriesFragment

@Module(includes = [APIServiceModule::class])
class FragmentModule {
    @Provides
//    @PerActivity
    fun provideEventCategoriesFragment(): EventCategoriesFragment {
        return EventCategoriesFragment()
    }
}