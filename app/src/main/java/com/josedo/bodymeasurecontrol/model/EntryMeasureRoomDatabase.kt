package com.josedo.bodymeasurecontrol.model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVReaderHeaderAware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
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
                ).addCallback(EntryMeasureDatabaseCallback(scope, context)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class EntryMeasureDatabaseCallback(
        private val scope: CoroutineScope, private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.entryMeasureDAO(), context)
                }
            }
        }

        suspend fun populateDatabase(entryMeasureDAO: EntryMeasureDAO, context: Context) {
            entryMeasureDAO.deleteAll()

            try {
                val inputStream: InputStream = context.getAssets().open( "data.csv" )

                val reader = CSVReaderBuilder(InputStreamReader(inputStream)).withCSVParser(
                    CSVParserBuilder()
                    .withSeparator(';')
                    .build()).build()

                var line: Array<String>? = reader.readNext()
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                while (line != null) {
                    // Do something with the data
                    //Log.d("populateDatabase", line[0])

                    val entryMeasure: EntryMeasure = EntryMeasure(simpleDateFormat.parse(line[0]), "","","", UnitMeasure.METRIC,
                        line[2].replace(',','.').toDouble(),line[4].replace(',','.').toDouble(),
                        line[3].replace(',','.').toDouble(),line[5].replace(',','.').toDouble(),
                        line[6].replace(',','.').toDouble(),line[1].replace(',','.').toDouble())
                    entryMeasureDAO.insert(entryMeasure)

                    line = reader.readNext()
                }

            } catch(ex: Exception) {
                ex.printStackTrace()
                Log.d("populateDatabase", ex.message)
            }

            /*var entryMeasure: EntryMeasure = EntryMeasure(Date(), "","","",UnitMeasure.METRIC, 102f,87f,89f,58f,32f,76.1f)
            entryMeasureDAO.insert(entryMeasure)

            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            var entryMeasure2: EntryMeasure = EntryMeasure( simpleDateFormat.parse("09/04/2020"), "","","",UnitMeasure.METRIC, 101.5f,87.5f,89.5f,59f,32f,76.2f)
            entryMeasureDAO.insert(entryMeasure2)*/
        }
    }
}