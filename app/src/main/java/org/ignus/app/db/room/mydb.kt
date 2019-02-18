package org.ignus.app.db.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ignus.app.App
import org.ignus.app.db.converters.EventListConverter
import org.ignus.app.db.converters.UserProfileConverter
import org.ignus.app.db.converters.WorkshopConverter
import org.ignus.app.db.models.*


@Database(
    entities = [EventCategory::class, Workshop::class, EventDetails::class, UserProfile::class, Message::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(EventListConverter::class, WorkshopConverter::class, UserProfileConverter::class)
abstract class MyDatabase : RoomDatabase() {
    @TypeConverters(EventListConverter::class)
    abstract fun eventCategoryDao(): EventCategoryDao

    @TypeConverters(WorkshopConverter::class)
    abstract fun workshopListDao(): WorkshopListDao

    abstract fun eventDetailsDao(): EventDetailsDao

    @TypeConverters(UserProfileConverter::class)
    abstract fun userProfileDao(): UserProfileDao

    abstract fun confessionDao(): ConfessionDao
}

val db = Room.databaseBuilder(App.instance, MyDatabase::class.java, "database")
    .allowMainThreadQueries()
    .fallbackToDestructiveMigration()
    .build()