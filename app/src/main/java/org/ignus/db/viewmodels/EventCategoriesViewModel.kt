package org.ignus.db.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableObserver
import org.ignus.db.models.EventCategory
import org.ignus.db.repositories.EventCategoryRepository

class EventCategoriesViewModel : ViewModel() {

    private val eventCategoryRepository by lazy { EventCategoryRepository() }
    var eventCategories: MutableLiveData<List<EventCategory>> = MutableLiveData()
        private set
    var eventCategoriesError: MutableLiveData<String> = MutableLiveData()
        private set

    private lateinit var disposableObserver: DisposableObserver<List<EventCategory>>

    fun refreshEventCategories() {
        disposableObserver =
                object : DisposableObserver<List<EventCategory>>() {

                    override fun onComplete() {}

                    override fun onNext(t: List<EventCategory>) {
                        Log.d("suthar", "onNext ${t.size}")
                        eventCategories.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("suthar", "onError")
                        eventCategoriesError.postValue(e.message)
                    }
                }

        eventCategoryRepository.getEventCategories().subscribe(disposableObserver)
    }

    fun getLoading(): MutableLiveData<Boolean> {
        return eventCategoryRepository.loading
    }

    fun disposeElements() {
        if (!disposableObserver.isDisposed) disposableObserver.dispose()
    }
}