package org.ignus.db.repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.ignus.App
import org.ignus.db.models.EventDetails
import org.ignus.db.room.EventDetailsDao
import org.ignus.db.room.db
import org.ignus.services.APIClient
import org.ignus.services.APIService
import java.util.concurrent.TimeUnit

class EventDetailsRepository {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    private val eventDetailsDao: EventDetailsDao by lazy { db.eventDetailsDao() }

    fun getEventDetails(id: String): Observable<EventDetails> {
        refreshEventCategories(id)
        return eventDetailsDao.get(id)
    }

    private fun refreshEventCategories(id: String): Disposable? {

        loading.postValue(true)

        return APIClient.getClient().create(APIService::class.java)
            .getEventDetails(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe({
                it.id = id
                eventDetailsDao.save(it)
                loading.postValue(false)
            }, {
                loading.postValue(false)
                Log.d("suthar", "Error:  $it")
                Toast.makeText(App.instance, "No Network Connectivity!", Toast.LENGTH_SHORT).show()
            })
    }
}