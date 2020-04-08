package com.josedo.bodymeasurecontrol.model

import androidx.room.*
import java.util.*

@Entity(tableName = "entrymeasure_table")
data class EntryMeasure(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "date_measure")
    val dateMeasure: Date,

    @ColumnInfo(name = "photos_url")
    val frontPhotoUrl: String,

    @ColumnInfo(name = "photos_url")
    val backPhotoUrl: String,

    @ColumnInfo(name = "photos_url")
    val sidePhotoUrl: String,

    @ColumnInfo(name = "system_unit")
    var systemUnit: UnitMeasure,

    @ColumnInfo(name = "chest_value")
    val chestValue: Float,

    @ColumnInfo(name = "waist_value")
    val waistValue: Float,

    @ColumnInfo(name = "hip_value")
    val hipValue: Float,

    @ColumnInfo(name = "leg_value")
    val legValue: Float,

    @ColumnInfo(name = "bicep_value")
    val bicepValue: Float,

    @ColumnInfo(name = "bodyWeight_value")
    val bodyWeightValue: Float
) {
}