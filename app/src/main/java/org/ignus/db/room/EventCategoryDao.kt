package org.ignus.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import org.ignus.db.models.EventCategory

@Dao
abstract class EventCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(eventCategory: EventCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveAll(list: List<EventCategory>)

    @Query("SELECT * FROM eventCategory WHERE name = :name")
    abstract fun get(name: String): Observable<EventCategory>

    @Query("SELECT * FROM eventCategory")
    abstract fun getAll(): Observable<List<EventCategory>>

    @Query("DELETE FROM eventCategory")
    abstract fun deleteAll()
}