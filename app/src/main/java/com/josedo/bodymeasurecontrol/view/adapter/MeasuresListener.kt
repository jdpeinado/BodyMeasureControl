package com.josedo.bodymeasurecontrol.view.adapter

import android.icu.util.Measure
import com.josedo.bodymeasurecontrol.model.EntryMeasure

interface MeasuresListener {
    fun onMeasureClick(position: Int)
}