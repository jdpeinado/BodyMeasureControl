package com.josedo.bodymeasurecontrol.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface EntryMeasureDAO {

    @Query("SELECT * from ${EntryMeasure.TABLE_NAME} ORDER BY date_measure DESC")
    fun getAllEntryMeasure(): LiveData<List<EntryMeasure>>

    @Query("SELECT * from ${EntryMeasure.TABLE_NAME} ORDER BY date_measure DESC LIMIT 2")
    fun getMostRecentEntryMeasure(): LiveData<List<EntryMeasure>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entryMeasure: EntryMeasure)

    @Delete
    suspend fun delete(entryMeasure: EntryMeasure)

    @Update
    suspend fun update(entryMeasure: EntryMeasure): Int

    @Query("DELETE FROM ${EntryMeasure.TABLE_NAME}")
    fun deleteAll()
}