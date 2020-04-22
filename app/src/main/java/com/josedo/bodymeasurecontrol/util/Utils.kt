package com.josedo.bodymeasurecontrol.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.UnitMeasure
import com.josedo.bodymeasurecontrol.view.ui.activity.MainActivity
import java.math.BigDecimal
import java.math.RoundingMode

class Utils {

    companion object {
        fun getRoundNumberDecimal(n: Double, roundTo: Int): BigDecimal {
            val decimal = BigDecimal(n).setScale(roundTo, RoundingMode.HALF_EVEN)

            return decimal
        }
    }
}