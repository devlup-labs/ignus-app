package org.ignus.app.db.repositories

import android.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.ignus.app.App
import org.ignus.app.db.models.Event
import org.ignus.app.db.models.TeamEvents
import org.ignus.app.db.models.Workshop
import org.ignus.app.services.APIClient
import org.ignus.app.services.APIService
import java.util.concurrent.TimeUnit

class RegisteredEventsRepository {

    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(App.instance) }
    private val token: String by lazy { sp.getString("jwt-token", "") ?: "" }

    fun getSoloRegisteredEvents(): Observable<List<Event>>? {

        return APIClient.getClient().create(APIService::class.java)
            .getSoloRegisteredEvents("JWT $token")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
    }

    fun getTeamRegisteredEvents(): Observable<List<TeamEvents>>? {

        return APIClient.getClient().create(APIService::class.java)
            .getTeamRegisteredEvents("JWT $token")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
    }

    fun getRegisteredWorkshops(): Observable<List<Workshop>>? {

        return APIClient.getClient().create(APIService::class.java)
            .getRegisteredWorkshops("JWT $token")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
    }
}