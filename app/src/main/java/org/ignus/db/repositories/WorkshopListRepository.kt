package org.ignus.db.repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.ignus.App
import org.ignus.db.models.Workshop
import org.ignus.db.room.WorkshopListDao
import org.ignus.db.room.db
import org.ignus.services.APIClient
import org.ignus.services.APIService
import java.util.concurrent.TimeUnit

class WorkshopListRepository {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    private val workshopListDao: WorkshopListDao by lazy { db.workshopListDao() }


    fun getWorkshopList(): Observable<List<Workshop>> {
        refreshWorkshopList()
        return workshopListDao.getAll()
    }

    private fun refreshWorkshopList(): Disposable? {

        loading.postValue(true)

        return APIClient.getClient().create(APIService::class.java)
            .getWorkshopList()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe({
                Log.d("suthar", "Workshop ${it.size}")
                workshopListDao.saveAll(it)
                loading.postValue(false)
            }, {
                loading.postValue(false)
                Log.d("suthar", "Error:  $it")
                Toast.makeText(App.instance, "Something went wrong!", Toast.LENGTH_LONG).show()
            })
    }

}
