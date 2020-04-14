package com.josedo.bodymeasurecontrol.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.josedo.bodymeasurecontrol.model.EntryMeasure
import com.josedo.bodymeasurecontrol.model.EntryMeasureRepository
import com.josedo.bodymeasurecontrol.model.EntryMeasureRoomDatabase
import com.josedo.bodymeasurecontrol.model.UnitMeasure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ShareViewModel(application: Application): AndroidViewModel(application) {
    private val repository: EntryMeasureRepository
    var allEntryMeasures: LiveData<List<EntryMeasure>>
    var isLoading = MutableLiveData<Boolean>()
    var entryMeasureToModify: MutableLiveData<EntryMeasure> = MutableLiveData<EntryMeasure>()
    var dateEditTextIsEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var modifyButtonIsEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var addButtonIsEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        Log.d("ShareViewModel", "init sharedviewmodel")
        val entryMeasureDao = EntryMeasureRoomDatabase.getDatabase(application, viewModelScope).entryMeasureDAO()
        repository = EntryMeasureRepository(entryMeasureDao)
        allEntryMeasures = repository.allEntryMeasures
    }

    fun refresh(){
        getAllEntryMeasures()
    }

    fun getAllEntryMeasures(){
        allEntryMeasures = repository.allEntryMeasures
        isLoading.value =  true
    }

    fun insert(entryMeasure: EntryMeasure) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entryMeasure)
    }

    fun update(entryMeasure: EntryMeasure) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(entryMeasure)
    }

    fun cleanDataInputFragment(){
        dateEditTextIsEnabled.postValue(true)
        modifyButtonIsEnabled.postValue(false)
        addButtonIsEnabled.postValue(true)
        val entryMeasure: EntryMeasure = EntryMeasure(
            Date(),"","","",
            UnitMeasure.METRIC, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        entryMeasureToModify.postValue(entryMeasure)
    }
}