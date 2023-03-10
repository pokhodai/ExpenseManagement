package com.android.pokhodai.expensemanagement.utils

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

@Parcelize
@Suppress("FunctionName")
class LocalDateFormatter(
    val localDateTime: LocalDateTime
) : Parcelable, Comparable<LocalDateFormatter> {

    @IgnoredOnParcel
    private val localeEu = Locale("en")

    fun format(pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(localeEu)
        return  localDateTime.format(formatter)
    }

    fun isItThisYear() = now().localDateTime.year == localDateTime.year

    fun dd_MM_yyyy() = format("dd.MM.yyyy")
    fun MMMM_yyyy() = format("MMMM yyyy")
    fun yyyy() = format("yyyy")
    fun MMMM() = format("MMMM")
    fun dd_MMMM_yyyy() = format("dd MMMM yyyy")
    fun toIsoFormatWithoutTZ() = format(ISO_FORMAT)

    fun timeInMillis() = localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    fun toIsoUTC(): String = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        .atOffset(ZoneOffset.ofHours(0))
        .format(DateTimeFormatter.ofPattern(ISO_FORMAT))

    inline fun update(block: LocalDateTime.() -> LocalDateTime): LocalDateFormatter {
        return LocalDateFormatter(localDateTime.block())
    }

    fun withLocalTime(other: LocalDateFormatter): LocalDateFormatter {
        return LocalDateFormatter(
            localDateTime.withHour(other.localDateTime.hour).withMinute(other.localDateTime.minute)
        )
    }

    fun between(other: LocalDateFormatter): Duration =
        Duration.between(localDateTime, other.localDateTime)

    override fun compareTo(other: LocalDateFormatter): Int {
        return timeInMillis().compareTo(other.timeInMillis())
    }

    override fun equals(other: Any?): Boolean =
        (other is LocalDateFormatter) && this.timeInMillis() == other.timeInMillis()

    fun equalsDate(other: LocalDateFormatter): Boolean =
        localDateTime.year == other.localDateTime.year
                && localDateTime.dayOfYear == other.localDateTime.dayOfYear

    override fun hashCode(): Int {
        return localDateTime.hashCode()
    }

    companion object {
        const val ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        fun now() = LocalDateFormatter(LocalDateTime.now())
        fun nowUtc() = LocalDateFormatter(LocalDateTime.now(ZoneOffset.UTC))
        fun nowWithZoneOffset(offsetInHours: Int): LocalDateFormatter {
            val utc = LocalDateTime.now(ZoneOffset.ofHours(offsetInHours))
            return LocalDateFormatter(utc)
        }

        fun today() = LocalDateFormatter(LocalDateTime.now().toLocalDate().atStartOfDay())
        fun from(millis: Long) = LocalDateFormatter(
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
        )

        fun parse(string: String?, pattern: String): LocalDateFormatter? {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val localDateTime: LocalDateTime =
                LocalDate.parse(string, formatter)?.atStartOfDay() ?: return null
            return LocalDateFormatter(localDateTime)
        }
    }
}