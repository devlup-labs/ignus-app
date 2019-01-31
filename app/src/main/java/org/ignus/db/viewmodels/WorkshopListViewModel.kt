package org.ignus.db.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableObserver
import org.ignus.db.models.Workshop
import org.ignus.db.repositories.WorkshopListRepository

class WorkshopListViewModel : ViewModel() {

    private val workshopListRepository by lazy { WorkshopListRepository() }
    var workshopList: MutableLiveData<List<Workshop>> = MutableLiveData()
        private set
    var workshopListError: MutableLiveData<String> = MutableLiveData()
        private set

    private lateinit var disposableObserver: DisposableObserver<List<Workshop>>

    fun refreshWorkshopList() {

        disposableObserver =
                object : DisposableObserver<List<Workshop>>() {

                    override fun onComplete() {}

                    override fun onNext(t: List<Workshop>) {
                        Log.d("suthar", "onNext ${t.size}")
                        workshopList.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("suthar", "onError")
                        workshopListError.postValue(e.message)
                    }
                }

        workshopListRepository.getWorkshopList().subscribe(disposableObserver)
    }

    fun getLoading(): MutableLiveData<Boolean> {
        return workshopListRepository.loading
    }

    fun disposeElements() {
        if (!disposableObserver.isDisposed) disposableObserver.dispose()
    }
}