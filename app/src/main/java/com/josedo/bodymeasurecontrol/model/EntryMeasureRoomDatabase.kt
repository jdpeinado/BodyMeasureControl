package com.josedo.bodymeasurecontrol.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(EntryMeasure::class), version = 1, exportSchema = false)
abstract class EntryMeasureRoomDatabase: RoomDatabase() {
    abstract fun entryMeasureDAO(): EntryMeasureDAO

    companion object {
        @Volatile
        private var INSTANCE: EntryMeasureRoomDatabase? = null

        fun getDatabase(context: Context): EntryMeasureRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EntryMeasureRoomDatabase::class.java,
                    "entrymeasure_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}