package org.ignus.app.db.repositories

import io.reactivex.Observable
import org.ignus.app.db.models.EventCategory
import org.ignus.app.db.models.EventCategoryDao
import org.ignus.app.services.APIService
import java.util.concurrent.Executor
import javax.inject.Inject

//@PerApplication
class EventCategoryRepository @Inject
constructor(private val apiService: APIService, private val eventCategoryDao: EventCategoryDao, private val executor: Executor) {
    fun getEventCategories(): Observable<List<EventCategory>> {
        refreshEventCategories()
        return eventCategoryDao.all()
    }

    private fun refreshEventCategories() {
        executor.execute {
            // Check if eventCategory data was fetched recently.
            if (eventCategoryDao.refreshRequired(15)) {
                val response = apiService.getEventCategories().execute()
                if (response.isSuccessful) {
                    val eventCategories = response.body()
                    eventCategories?.map { eventCategoryDao.save(it) }
                }
                EventCategoryDao.lastUpdate = System.currentTimeMillis()
            }
        }
    }
}