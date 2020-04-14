package com.josedo.bodymeasurecontrol.model

import android.content.Context
import com.josedo.bodymeasurecontrol.R

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
}