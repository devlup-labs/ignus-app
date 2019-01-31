package org.ignus.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import org.ignus.db.models.Workshop

@Dao
abstract class WorkshopListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(workshop: Workshop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveAll(list: List<Workshop>)

    @Query("SELECT * FROM workshop WHERE name = :name")
    abstract fun get(name: String): Observable<Workshop>

    @Query("SELECT * FROM workshop")
    abstract fun getAll(): Observable<List<Workshop>>
}