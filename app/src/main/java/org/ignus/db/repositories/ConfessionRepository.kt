package org.ignus.db.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import org.ignus.db.models.Message
import org.ignus.db.room.ConfessionDao
import org.ignus.db.room.db

class ConfessionRepository {

    private val confessionDao: ConfessionDao by lazy { db.confessionDao() }
    private val ref = FirebaseDatabase.getInstance().getReference("confession/messages/")

    init {
        registerFirebaseListener()
    }

    fun getMessages(): Observable<List<Message>> {
        return confessionDao.getAll()
    }

    private fun registerFirebaseListener() {

        ref.keepSynced(true)
        ref.limitToLast(100).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val messages: ArrayList<Message> = ArrayList()
                for (child in p0.children) {
                    val text = child.child("message").value.toString()
                    val ig = child.child("username").value.toString()
                    val timestamp = child.child("timestamp").value?.toString()?.toLong() ?: 0
                    val message = Message(text, ig, timestamp)
                    messages.add(message)
                }
                confessionDao.delete()
                confessionDao.save(messages)
            }
        })
    }
}