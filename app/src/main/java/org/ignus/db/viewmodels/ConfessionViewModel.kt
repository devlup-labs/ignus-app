package org.ignus.db.viewmodels

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.disposables.Disposable
import org.ignus.App
import org.ignus.db.models.Message
import org.ignus.db.repositories.ConfessionRepository

class ConfessionViewModel : ViewModel() {

    private val repo by lazy { ConfessionRepository() }
    val messages: MutableLiveData<List<Message>> = MutableLiveData()
    private val ref = FirebaseDatabase.getInstance().getReference("confession/messages/")

    init {
        listenMessages()
    }

    private fun listenMessages(): Disposable? {
        return repo.getMessages().subscribe({
            messages.postValue(it)
        }, {
            Toast.makeText(App.instance, "Error getting new messages", Toast.LENGTH_SHORT).show()
        })
    }

    fun sendNewMessage(message: Message) {
        ref.push().setValue(message)
    }

    fun deleteMessage(key: String) {
        ref.child(key).removeValue()
    }
}