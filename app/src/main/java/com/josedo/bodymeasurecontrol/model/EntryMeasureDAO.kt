package com.josedo.bodymeasurecontrol.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EntryMeasureDAO {

    @Query("SELECT * from entrymeasure_table")
    fun getAllEntryMeasure(): LiveData<List<EntryMeasure>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entryMeasure: EntryMeasure)

    @Delete
    suspend fun delete(entryMeasure: EntryMeasure)

    @Update
    suspend fun update(entryMeasure: EntryMeasure): Int
}