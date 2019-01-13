package org.ignus.app.db.models

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ignus.app.db.models.converters.EventListConverter

@Database(entities = [EventCategory::class], version = 1, exportSchema = false)
@TypeConverters(EventListConverter::class)
abstract class UserProfileDatabase : RoomDatabase() {
    abstract fun eventCategoryDao(): EventCategoryDao
}