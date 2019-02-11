package org.ignus.db.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableObserver
import org.ignus.db.models.UserProfile
import org.ignus.db.repositories.UserProfileRepository

class LoginViewModel : ViewModel() {

    private val userProfileRepository by lazy { UserProfileRepository() }
    var userProfile: MutableLiveData<UserProfile> = MutableLiveData()

    private lateinit var disposableObserver: DisposableObserver<UserProfile>

    fun refreshUserProfile(username: String, password: String) {
        initObserver()
        userProfileRepository.getUserProfile(username, password).subscribe(disposableObserver)
    }

    fun refreshUserProfile() {
        initObserver()
        userProfileRepository.getUserProfile().subscribe(disposableObserver)
    }

    fun getLoading() = userProfileRepository.loading

    fun getSuccess() = userProfileRepository.success

    private fun initObserver() {
        disposableObserver =
            object : DisposableObserver<UserProfile>() {

                override fun onComplete() {}

                override fun onNext(t: UserProfile) {
                    Log.d("suthar", "onNext $t")
                    userProfile.postValue(t)
                }

                override fun onError(e: Throwable) {
                    Log.d("suthar", "onError $e")
                }
            }
    }

}