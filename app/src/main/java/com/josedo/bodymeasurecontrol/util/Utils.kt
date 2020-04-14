package com.josedo.bodymeasurecontrol.util

import java.math.BigDecimal
import java.math.RoundingMode

class Utils {

    companion object {
        fun getRoundNumberDecimal(n: Double): BigDecimal{
            val decimal = BigDecimal(n).setScale(1, RoundingMode.HALF_EVEN)

            return decimal
        }
    }
}