package org.ignus.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import org.ignus.db.models.UserProfile

@Dao
abstract class UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(userProfile: UserProfile)

    @Query("SELECT * FROM UserProfile")
    abstract fun get(): Observable<UserProfile>
}