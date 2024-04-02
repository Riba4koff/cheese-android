package ru.antares.cheese_android.presentation.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * ParsePrice.kt
 * @author Павел
 * Created by on 22.02.2024 at 19:37
 * Android studio
 */

fun parsePrice(price: Double): String {
    val df = DecimalFormat("###,##0")

    val customSymbol = DecimalFormatSymbols()
    customSymbol.groupingSeparator = ' '
    df.decimalFormatSymbols = customSymbol

    return df.format(price)
}