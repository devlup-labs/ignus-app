package org.ignus.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import org.ignus.db.models.EventDetails

@Dao
abstract class EventDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(eventDetails: EventDetails)

    @Query("SELECT * FROM eventDetails WHERE id = :id")
    abstract fun get(id: String): Observable<EventDetails>
}