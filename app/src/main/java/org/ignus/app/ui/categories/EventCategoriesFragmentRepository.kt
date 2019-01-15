package org.ignus.app.ui.categories

import androidx.lifecycle.MutableLiveData
import org.ignus.app.db.models.EventCategory


class EventCategoriesFragmentRepository {

    companion object {
        private var instance: EventCategoriesFragmentRepository? = null

        fun getInstance(): EventCategoriesFragmentRepository? {
            if (instance == null) instance = EventCategoriesFragmentRepository()
            return instance
        }
    }

    private var eventCategories: MutableLiveData<List<EventCategory>> = MutableLiveData()

    private fun setEventCategories() {
        // Make an network call and get Data
        // DaggerAPIServiceComponent
        val data = MutableLiveData<List<EventCategory>>()
    }

    fun getEventCategories(): MutableLiveData<List<EventCategory>> {
        setEventCategories()
        return eventCategories
    }
}