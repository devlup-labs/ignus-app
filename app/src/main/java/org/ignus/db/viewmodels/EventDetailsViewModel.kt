package org.ignus.db.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableObserver
import org.ignus.db.models.EventDetails
import org.ignus.db.repositories.EventDetailsRepository

class EventDetailsViewModel : ViewModel() {

    private val eventDetailsRepository by lazy { EventDetailsRepository() }
    val eventDetails: MutableLiveData<EventDetails> = MutableLiveData()
    val eventDetailsError: MutableLiveData<String> = MutableLiveData()

    private lateinit var disposableObserver: DisposableObserver<EventDetails>

    fun refreshEventCategories(id: String) {
        disposableObserver =
            object : DisposableObserver<EventDetails>() {

                override fun onComplete() {}

                override fun onNext(t: EventDetails) {
                    if (mID == t.id) eventDetails.postValue(t)
                }

                override fun onError(e: Throwable) {
                    eventDetailsError.postValue(e.message)
                }
            }

        mID = id
        val eventDetail = EventDetails(id, "", "", "", "")
        eventDetails.postValue(eventDetail)
        eventDetailsRepository.getEventDetails(id).subscribe(disposableObserver)
    }

    fun getLoading(): MutableLiveData<Boolean> {
        return eventDetailsRepository.loading
    }

    fun disposeElements() {
        if (!disposableObserver.isDisposed) disposableObserver.dispose()
    }

    companion object {
        private var mID: String = ""
    }
}