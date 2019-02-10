package org.ignus.db.repositories

import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.ignus.App
import org.ignus.db.models.UserProfile
import org.ignus.db.room.db
import org.ignus.services.APIClient
import org.ignus.services.APIService
import java.util.concurrent.TimeUnit

class UserProfileRepository {

    private val userProfileDao by lazy { db.userProfileDao() }
    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(App.instance) }

    fun getUserProfile(username: String, password: String): Observable<UserProfile> {
        refreshJWTToken(username, password)
        return userProfileDao.get()
    }

    fun getUserProfile(): Observable<UserProfile> {
        val token: String = sp.getString("jwt-token", "") ?: ""
        refreshUserProfile(token)
        return userProfileDao.get()
    }

    private fun refreshJWTToken(username: String, password: String): Disposable? {
        return APIClient.getClient().create(APIService::class.java)
            .getUserToken(username, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe({
                Log.d("suthar", "JWT $it")
                sp.edit().putString("jwt-token", it.get("token").asString).apply()
                refreshUserProfile(it.get("token").asString)
            }, {
                Log.d("suthar-repo", "Error JWT :  $it")
                Toast.makeText(App.instance, "User is not logged in!", Toast.LENGTH_LONG).show()
            })
    }

    private fun refreshUserProfile(access_token: String): Disposable? {

        return APIClient.getClient().create(APIService::class.java)
            .getUserProfile("JWT $access_token")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe({
                Log.d("suthar", "User Profile $it")
                userProfileDao.save(it)
            }, {
                Log.d("suthar-repo", "Error User Profile :  $it")
                Toast.makeText(App.instance, "No internet connectivity!", Toast.LENGTH_LONG).show()
            })
    }
}