package org.ignus.app.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.ignus.app.db.models.EventCategory

class EventCategoriesFragmentViewModel : ViewModel() {

    private lateinit var repo: EventCategoriesFragmentRepository
    var eventCategories: MutableLiveData<List<EventCategory>>? = null

    fun init() {
        if (eventCategories != null) return
        repo = EventCategoriesFragmentRepository.getInstance()!!
        eventCategories = repo.getEventCategories()
    }
}