package ru.antares.cheese_android.domain

import kotlinx.datetime.LocalDateTime
import java.time.DayOfWeek

/**
 * TimestampParser.kt
 * @author Павел
 * Created by on 06.04.2024 at 22:48
 * Android studio
 */

class TimestampParser {
    private companion object {
        private const val RUSSIAN_MONDAY = "Понедельник"
        private const val RUSSIAN_TUESDAY = "Вторник"
        private const val RUSSIAN_WEDNESDAY = "Среда"
        private const val RUSSIAN_THURSDAY = "Четверг"
        private const val RUSSIAN_FRIDAY = "Пятница"
        private const val RUSSIAN_SATURDAY = "Суббота"
        private const val RUSSIAN_SUNDAY = "Воскресенье"

        private const val RUSSIAN_JANUARY = "Января"
        private const val RUSSIAN_FEBRUARY = "Февраля"
        private const val RUSSIAN_MARCH = "Марта"
        private const val RUSSIAN_APRIL = "Апреля"
        private const val RUSSIAN_MAY = "Мая"
        private const val RUSSIAN_JUNE = "Июня"
        private const val RUSSIAN_JULY = "Июля"
        private const val RUSSIAN_AUGUST = "Августа"
        private const val RUSSIAN_SEPTEMBER = "Сентября"
        private const val RUSSIAN_OCTOBER = "Октября"
        private const val RUSSIAN_NOVEMBER = "Ноября"
        private const val RUSSIAN_DECEMBER = "Декабря"
    }
    operator fun invoke(timestamp: String?): String {
        if (timestamp == null) return ""

        val localDateTime = LocalDateTime.parse(timestamp)

        val dayOfWeek = getDayOfWeek(localDateTime.dayOfWeek)
        val dayOfMonth = localDateTime.dayOfMonth

        val month = getMonth(localDateTime.monthNumber)

        val hour = getHour(localDateTime.time.hour)
        val minute = getMinute(localDateTime.time.minute)

        val time = "$hour:$minute"


        return "$dayOfWeek, $dayOfMonth $month, $time"
    }

    private fun getDayOfWeek(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> RUSSIAN_MONDAY
            DayOfWeek.TUESDAY -> RUSSIAN_TUESDAY
            DayOfWeek.WEDNESDAY -> RUSSIAN_WEDNESDAY
            DayOfWeek.THURSDAY -> RUSSIAN_THURSDAY
            DayOfWeek.FRIDAY -> RUSSIAN_FRIDAY
            DayOfWeek.SATURDAY -> RUSSIAN_SATURDAY
            DayOfWeek.SUNDAY -> RUSSIAN_SUNDAY
        }
    }

    private fun getMonth(month: Int): String {
        return when (month) {
            1 -> RUSSIAN_JANUARY
            2 -> RUSSIAN_FEBRUARY
            3 -> RUSSIAN_MARCH
            4 -> RUSSIAN_APRIL
            5 -> RUSSIAN_MAY
            6 -> RUSSIAN_JUNE
            7 -> RUSSIAN_JULY
            8 -> RUSSIAN_AUGUST
            9 -> RUSSIAN_SEPTEMBER
            10 -> RUSSIAN_OCTOBER
            11 -> RUSSIAN_NOVEMBER
            12 -> RUSSIAN_DECEMBER
            else -> ""
        }
    }

    private fun getHour(hour: Int): String {
        return when (hour) {
            in 0..9 -> "0$hour"
            else -> "$hour"
        }
    }

    private fun getMinute(minute: Int): String {
        return when (minute) {
            in 0..9 -> "0$minute"
            else -> "$minute"
        }
    }
}