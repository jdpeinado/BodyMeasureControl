package com.josedo.bodymeasurecontrol.model

import androidx.room.TypeConverter

class UnitMeasureConverter {

    @TypeConverter
    fun fromUnitMeasure(value: UnitMeasure): Int{
        return value.ordinal
    }

    @TypeConverter
    fun toUnitMeasure(value: Int): UnitMeasure{
        return when(value){
            0 -> UnitMeasure.METRIC
            1 -> UnitMeasure.IMPERIAL
            else -> UnitMeasure.METRIC
        }
    }
}