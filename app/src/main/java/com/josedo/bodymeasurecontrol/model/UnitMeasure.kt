package com.josedo.bodymeasurecontrol.model

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.util.Utils

enum class UnitMeasure(val value: Int) {
    METRIC(0),
    IMPERIAL(1);

    fun getWeightFormat(context: Context):String {
        if(this==IMPERIAL){
            return context.getString(R.string.lb_label)
        }
        return context.getString(R.string.kg_label)
    }

    fun getSizeFormat(context: Context):String {
        if(this==IMPERIAL){
            return context.getString(R.string.inch_label)
        }
        return context.getString(R.string.cm_label)
    }

    companion object{
        fun fromKgToLb(context: Context, data: Double): Double {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metric_system = prefs.getString(
                context.getString(R.string.metric_system_key),
                UnitMeasure.METRIC.value.toString()
            )

            if (metric_system != null) {
                if (metric_system.toInt().equals(UnitMeasure.METRIC.value)){
                    return data
                }else {
                    return data * 2.20462
                }
            } else {
                return data
            }
        }

        fun fromLbTokg(context: Context, data: Double): Double {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metric_system = prefs.getString(
                context.getString(R.string.metric_system_key),
                METRIC.value.toString()
            )

            if (metric_system != null) {
                if (metric_system.toInt().equals(METRIC.value)){
                    return data
                }else {
                    return data / 2.20462
                }
            } else {
                return data
            }
        }

        fun fromKgToLbString(context: Context, data: Double): String {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metric_system = prefs.getString(
                context.getString(R.string.metric_system_key),
                UnitMeasure.METRIC.value.toString()
            )

            if (metric_system != null) {
                if (metric_system.toInt().equals(UnitMeasure.METRIC.value)){
                    val unitMeasure: UnitMeasure = UnitMeasure.METRIC
                    return context.resources.getString(
                        R.string.measure_presentation,
                        Utils.getRoundNumberDecimal(data,1).toString(),
                        unitMeasure.getWeightFormat(
                            context
                        ))
                }else {
                    val unitMeasure: UnitMeasure = UnitMeasure.IMPERIAL
                    return context.resources.getString(
                        R.string.measure_presentation,
                        Utils.getRoundNumberDecimal(data * 2.20462,1).toString(),
                        unitMeasure.getWeightFormat(
                            context
                        ))
                }
            } else {
                val unitMeasure: UnitMeasure = UnitMeasure.METRIC
                return context.resources.getString(
                    R.string.measure_presentation,
                    Utils.getRoundNumberDecimal(data,1).toString(),
                    unitMeasure.getWeightFormat(
                        context
                    )
                )
            }
        }

        fun fromCmToIn(context: Context, data: Double): Double {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metric_system = prefs.getString(
                context.getString(R.string.metric_system_key),
                UnitMeasure.METRIC.value.toString()
            )

            if (metric_system != null) {
                if (metric_system.toInt().equals(UnitMeasure.METRIC.value)){
                    return data
                }else {
                    return data / 2.54
                }
            } else {
                return data
            }
        }

        fun fromInToCm(context: Context, data: Double): Double {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metric_system = prefs.getString(
                context.getString(R.string.metric_system_key),
                UnitMeasure.METRIC.value.toString()
            )

            if (metric_system != null) {
                if (metric_system.toInt().equals(UnitMeasure.METRIC.value)){
                    return data
                }else {
                    return data * 2.54
                }
            } else {
                return data
            }
        }

        fun fromCmToInString(context: Context, data: Double): String {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metric_system = prefs.getString(
                context.getString(R.string.metric_system_key),
                UnitMeasure.METRIC.value.toString()
            )

            if (metric_system != null) {
                if (metric_system.toInt().equals(UnitMeasure.METRIC.value)){
                    val unitMeasure: UnitMeasure = UnitMeasure.METRIC
                    return context.resources.getString(
                        R.string.measure_presentation,
                        Utils.getRoundNumberDecimal(data,1).toString(),
                        unitMeasure.getSizeFormat(
                            context
                        ))
                }else {
                    val unitMeasure: UnitMeasure = UnitMeasure.IMPERIAL
                    return context.resources.getString(
                        R.string.measure_presentation,
                        Utils.getRoundNumberDecimal(data / 2.54,1).toString(),
                        unitMeasure.getSizeFormat(
                            context
                        ))
                }
            } else {
                val unitMeasure: UnitMeasure = UnitMeasure.METRIC
                return context.resources.getString(
                    R.string.measure_presentation,
                    Utils.getRoundNumberDecimal(data,1).toString(),
                    unitMeasure.getSizeFormat(
                        context
                    )
                )
            }
        }

        fun fromMToFtString(context: Context, data: Double): String {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metric_system = prefs.getString(
                context.getString(R.string.metric_system_key),
                UnitMeasure.METRIC.value.toString()
            )

            if (metric_system != null) {
                if (metric_system.toInt().equals(UnitMeasure.METRIC.value)){
                    return Utils.getRoundNumberDecimal(data,1).toString()
                }else {
                    return Utils.getRoundNumberDecimal(data * 3.28084,1).toString()
                }
            } else {
                return Utils.getRoundNumberDecimal(data,1).toString()
            }
        }

        fun fromFtToMString(context: Context, data: Double): String {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metric_system = prefs.getString(
                context.getString(R.string.metric_system_key),
                UnitMeasure.METRIC.value.toString()
            )

            if (metric_system != null) {
                if (metric_system.toInt().equals(UnitMeasure.METRIC.value)){
                    return Utils.getRoundNumberDecimal(data,1).toString()
                }else {
                    return Utils.getRoundNumberDecimal(data / 3.28084,1).toString()
                }
            } else {
                return Utils.getRoundNumberDecimal(data,1).toString()
            }
        }
    }
}