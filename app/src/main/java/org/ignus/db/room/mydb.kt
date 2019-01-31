package org.ignus.db.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ignus.App
import org.ignus.db.converters.EventListConverter
import org.ignus.db.converters.WorkshopConverter
import org.ignus.db.models.EventCategory
import org.ignus.db.models.EventDetails
import org.ignus.db.models.Workshop


@Database(entities = [EventCategory::class, Workshop::class, EventDetails::class], version = 1, exportSchema = false)
@TypeConverters(EventListConverter::class, WorkshopConverter::class)
abstract class MyDatabase : RoomDatabase() {
    @TypeConverters(EventListConverter::class)
    abstract fun eventCategoryDao(): EventCategoryDao

    @TypeConverters(WorkshopConverter::class)
    abstract fun workshopListDao(): WorkshopListDao

    abstract fun eventDetailsDao(): EventDetailsDao
}

val db = Room.databaseBuilder(App.instance, MyDatabase::class.java, "database").allowMainThreadQueries().build()