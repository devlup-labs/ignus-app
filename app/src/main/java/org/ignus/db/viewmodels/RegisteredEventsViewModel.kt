package org.ignus.db.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import org.ignus.App
import org.ignus.db.models.Event
import org.ignus.db.models.Workshop
import org.ignus.db.repositories.RegisteredEventsRepository

class RegisteredEventsViewModel : ViewModel() {

    private val repository by lazy { RegisteredEventsRepository() }

    val soloEvents = MutableLiveData<List<Event>>()
    val teamEvents = MutableLiveData<List<Event>>()
    val workshops = MutableLiveData<List<Workshop>>()
    val loading = MutableLiveData<Boolean>()

    private var loading0 = true
    private var loading1 = true
    private var loading2 = true

    fun refreshRegisteredEvents() {

        loading0 = true
        loading1 = true
        loading2 = true

        getSoloRegisteredEvents()
        getTeamRegisteredEvents()
        getRegisteredWorkshops()
    }

    private fun getSoloRegisteredEvents(): Disposable? {
        return repository.getSoloRegisteredEvents()?.subscribe({
            soloEvents.postValue(it)
            loading0 = false
            updateLoading()
        }, {
            loading0 = false
            updateLoading()
            showError(it)
        })
    }

    private fun getTeamRegisteredEvents(): Disposable? {
        return repository.getTeamRegisteredEvents()?.subscribe({
            teamEvents.postValue(it)
            loading1 = false
            updateLoading()
        }, {
            loading1 = false
            updateLoading()
            showError(it)
        })
    }

    private fun getRegisteredWorkshops(): Disposable? {
        return repository.getRegisteredWorkshops()?.subscribe({
            workshops.postValue(it)
            loading2 = false
            updateLoading()
        }, {
            loading2 = false
            updateLoading()
            showError(it)
        })
    }

    private fun updateLoading() {
        val x = loading0 || loading1 || loading2
        loading.postValue(x)
    }

    private fun showError(it: Throwable) {
        Log.d("suthar", "Error, $it")
        Toast.makeText(App.instance, "No internet connectivity!", Toast.LENGTH_SHORT).show()
    }
}