package com.mog.bondoman.utils

import android.icu.text.DecimalFormat

object ValueViewConverter {

    fun doubleToString(double: Double): String {
        val decimalFormat = DecimalFormat("#")
        decimalFormat.maximumFractionDigits = 2
        return decimalFormat.format(double)
    }
}