package org.ignus.db.repositories

import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
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

    val loading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()
    private val userProfileDao by lazy { db.userProfileDao() }
    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(App.instance) }

    fun getUserProfile(username: String, password: String): Observable<UserProfile> {
        loading.postValue(true)
        refreshJWTToken(username, password)
        return userProfileDao.get()
    }

    fun getUserProfile(): Observable<UserProfile> {
        loading.postValue(true)
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
                sp.edit().putString("jwt-token", it.get("token").asString).apply()
                refreshUserProfile(it.get("token").asString)
            }, {
                loading.postValue(false)
                Toast.makeText(App.instance, "Username/Password do not match!", Toast.LENGTH_LONG).show()
            })
    }

    private fun refreshUserProfile(access_token: String): Disposable? {

        return APIClient.getClient().create(APIService::class.java)
            .getUserProfile("JWT $access_token")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe({
                success.postValue(true)
                loading.postValue(false)
                userProfileDao.save(it)
            }, {
                when {
                    it.message?.contains("401") == true -> showToast("Unauthorized User")
                    it.message?.contains("403") == true -> {
                        sp.edit().remove("jwt-token").apply()
                        showToast("Token expired please login again")
                    }
                    else -> showToast("Something wrong happened!")
                }
                Log.d("suthar", "Error, ${it.message}")
                loading.postValue(false)
            })
    }

    private fun showToast(string: String) {
        Toast.makeText(App.instance, string, Toast.LENGTH_SHORT).show()
    }
}