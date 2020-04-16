package com.josedo.bodymeasurecontrol.model

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.josedo.bodymeasurecontrol.util.ImageStorageManager
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVReaderHeaderAware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

@Database(entities = arrayOf(EntryMeasure::class), version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, UnitMeasureConverter::class)
abstract class EntryMeasureRoomDatabase : RoomDatabase() {
    abstract fun entryMeasureDAO(): EntryMeasureDAO

    companion object {
        private const val DATABASE_NAME = "entrymeasure_table"

        @Volatile
        private var INSTANCE: EntryMeasureRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): EntryMeasureRoomDatabase {
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
                val inputStream: InputStream = context.getAssets().open("data.csv")

                val reader = CSVReaderBuilder(InputStreamReader(inputStream)).withCSVParser(
                    CSVParserBuilder()
                        .withSeparator(';')
                        .build()
                ).build()

                var line: Array<String>? = reader.readNext()
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                while (line != null) {
                    // Do something with the data
                    //Log.d("populateDatabase", line[0])

                    var frontPhotoUrl = ""
                    var backPhotoUrl = ""
                    var sidePhotoUrl = ""
                    if (line[0].equals("11/04/2020")) {
                        try {
                            val isFrontPhoto: InputStream = context.getAssets().open("front.jpeg")
                            val bmpFront = BitmapFactory.decodeStream(isFrontPhoto)
                            val nameFrontImage = line[0].replace('/','-') + "_front.jpeg"
                            frontPhotoUrl = ImageStorageManager.saveToInternalStorage(
                                context,
                                bmpFront,
                                nameFrontImage
                            )
                        } catch (ex: FileNotFoundException) {
                            ex.printStackTrace()
                            frontPhotoUrl = ""
                        }

                        try {
                            val isBackPhoto: InputStream = context.getAssets().open("back.jpeg")
                            val bmpBack = BitmapFactory.decodeStream(isBackPhoto)
                            val nameBackImage = line[0].replace('/','-') + "_back.jpeg"
                            backPhotoUrl = ImageStorageManager.saveToInternalStorage(
                                context,
                                bmpBack,
                                nameBackImage
                            )
                        } catch (ex: FileNotFoundException) {
                            ex.printStackTrace()
                            backPhotoUrl = ""
                        }

                        try {
                            val isSidePhoto: InputStream = context.getAssets().open("side.jpeg")
                            val bmpSide = BitmapFactory.decodeStream(isSidePhoto)
                            val nameSideImage = line[0].replace('/','-') + "_side.jpeg"
                            sidePhotoUrl = ImageStorageManager.saveToInternalStorage(
                                context,
                                bmpSide,
                                nameSideImage
                            )
                        } catch (ex: FileNotFoundException) {
                            ex.printStackTrace()
                            sidePhotoUrl = ""
                        }
                    }

                    val entryMeasure: EntryMeasure = EntryMeasure(
                        simpleDateFormat.parse(line[0]),
                        frontPhotoUrl,
                        backPhotoUrl,
                        sidePhotoUrl,
                        UnitMeasure.METRIC,
                        line[2].replace(',', '.').toDouble(),
                        line[4].replace(',', '.').toDouble(),
                        line[3].replace(',', '.').toDouble(),
                        line[5].replace(',', '.').toDouble(),
                        line[6].replace(',', '.').toDouble(),
                        line[1].replace(',', '.').toDouble()
                    )
                    entryMeasureDAO.insert(entryMeasure)

                    line = reader.readNext()
                }

            } catch (ex: Exception) {
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