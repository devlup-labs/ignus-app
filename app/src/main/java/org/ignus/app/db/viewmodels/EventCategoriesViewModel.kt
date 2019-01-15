package org.ignus.app.db.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import io.reactivex.BackpressureStrategy
import org.ignus.app.db.models.EventCategory
import org.ignus.app.db.repositories.EventCategoryRepository
import javax.inject.Inject

class EventCategoriesViewModel
@Inject
constructor(private val eventCategoryRepository: EventCategoryRepository) : ViewModel() {
    var eventCategories: LiveData<List<EventCategory>>? = null
        private set

    init {
        eventCategories = LiveDataReactiveStreams.fromPublisher(
                eventCategoryRepository.getEventCategories().toFlowable(BackpressureStrategy.LATEST)
        )
    }
}