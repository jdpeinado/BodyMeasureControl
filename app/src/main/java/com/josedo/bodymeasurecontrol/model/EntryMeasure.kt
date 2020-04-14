package com.josedo.bodymeasurecontrol.model

import androidx.room.*
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.util.*

@Entity(tableName = EntryMeasure.TABLE_NAME)
class EntryMeasure(

    @ColumnInfo(name = "date_measure")
    @NotNull
    var dateMeasure: Date,

    @ColumnInfo(name = "front_photos_url")
    var frontPhotoUrl: String,

    @ColumnInfo(name = "back_photos_url")
    var backPhotoUrl: String,

    @ColumnInfo(name = "side_photos_url")
    var sidePhotoUrl: String,

    @ColumnInfo(name = "system_unit")
    @NotNull
    var systemUnit: UnitMeasure,

    @ColumnInfo(name = "chest_value")
    var chestValue: Double,

    @ColumnInfo(name = "waist_value")
    var waistValue: Double,

    @ColumnInfo(name = "hip_value")
    var hipValue: Double,

    @ColumnInfo(name = "leg_value")
    var legValue: Double,

    @ColumnInfo(name = "bicep_value")
    var bicepValue: Double,

    @ColumnInfo(name = "bodyWeight_value")
    var bodyWeightValue: Double

): Serializable{
    companion object {
        const val TABLE_NAME = "entrymeasure_table"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}