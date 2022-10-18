package com.lyh.carexplorer.feature.user.mapper

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun LocalDate.toIsoDate(): String = DateTimeFormatter.ISO_DATE.format(this)

fun String.toEpochTimestamp(): Long? {
    if (this.isBlank()) {
        return null
    }
    val localDate = LocalDate.from(DateTimeFormatter.ISO_DATE.parse(this))

    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
