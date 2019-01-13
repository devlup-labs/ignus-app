package org.ignus.app.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

@Dao
abstract class EventCategoryDao {
    companion object {
        var lastUpdate = System.currentTimeMillis()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(eventCategory: EventCategory)

    @Query("SELECT * FROM eventCategory WHERE name = :name")
    abstract fun get(name: String): Observable<EventCategory>

    @Query("SELECT * FROM eventCategory")
    abstract fun all(): Observable<List<EventCategory>>

    fun refreshRequired(timeout: Int): Boolean {
        return lastUpdate + timeout > System.currentTimeMillis()
    }
}