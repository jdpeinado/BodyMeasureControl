package com.josedo.bodymeasurecontrol.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Database(entities = arrayOf(EntryMeasure::class), version = 1, exportSchema = false)
@TypeConverters(DateConverter::class,UnitMeasureConverter::class)
abstract class EntryMeasureRoomDatabase: RoomDatabase() {
    abstract fun entryMeasureDAO(): EntryMeasureDAO

    companion object {
        private const val DATABASE_NAME = "entrymeasure_table"
        @Volatile
        private var INSTANCE: EntryMeasureRoomDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): EntryMeasureRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EntryMeasureRoomDatabase::class.java,
                    DATABASE_NAME
                ).addCallback(EntryMeasureDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class EntryMeasureDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.entryMeasureDAO())
                }
            }
        }

        suspend fun populateDatabase(entryMeasureDAO: EntryMeasureDAO) {
            entryMeasureDAO.deleteAll()

            var entryMeasure: EntryMeasure = EntryMeasure(Date(), "","","",UnitMeasure.METRIC, 102f,87f,89f,58f,32f,76f)
            entryMeasureDAO.insert(entryMeasure)

            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            var entryMeasure2: EntryMeasure = EntryMeasure( simpleDateFormat.parse("09/04/2020"), "","","",UnitMeasure.METRIC, 101.5f,87.5f,89.5f,59f,32f,77f)
            entryMeasureDAO.insert(entryMeasure2)
        }
    }
}