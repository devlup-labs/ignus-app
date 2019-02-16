package org.ignus.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import org.ignus.db.models.Message

@Dao
abstract class ConfessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(messages: List<Message>)

    @Query("SELECT * FROM Message")
    abstract fun getAll(): Observable<List<Message>>

    @Query("DELETE FROM Message")
    abstract fun delete()
}