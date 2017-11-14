package com.hendal.spring.jdbcextensions.jdbc

import java.sql.ResultSet
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ResultSet.getLocalDate(field: String) = LocalDate.from(
        DateTimeFormatter.ISO_DATE.parse(getString(field))
)!!

fun ResultSet.getLocalDateTime(field: String) = LocalDateTime.from(
        DateTimeFormatter.ISO_DATE_TIME.parse(getString(field))
)!!

fun ResultSet.getZonedDateTime(field: String) = ZonedDateTime.from(
        DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(getString(field))
)!!

fun ResultSet.getDuration(field: String) = Duration.parse(
        getString(field)
)!!

fun <T> ResultSet.getTypedArray(field: String) =
        getArray(field).array!! as Array<T>