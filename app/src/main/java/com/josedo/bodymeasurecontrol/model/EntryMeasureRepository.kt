package com.josedo.bodymeasurecontrol.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EntryMeasureRepository(private val entryMeasureDAO: EntryMeasureDAO) {

    val allEntryMeasures: LiveData<List<EntryMeasure>> = entryMeasureDAO.getAllEntryMeasure()

    suspend fun insert(entryMeasure: EntryMeasure) {
        entryMeasureDAO.insert(entryMeasure)
    }

    suspend fun delete(entryMeasure: EntryMeasure){
        entryMeasureDAO.delete(entryMeasure)
    }

    suspend fun update(entryMeasure: EntryMeasure): Int{
        return entryMeasureDAO.update(entryMeasure)
    }
}