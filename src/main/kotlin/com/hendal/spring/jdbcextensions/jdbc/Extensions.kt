package com.hendal.spring.jdbcextensions.jdbc

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.sql.ResultSet
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

private val dateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE)
        .optionalStart().appendLiteral('T').optionalEnd()
        .optionalStart().appendLiteral(' ').optionalEnd()
        .append(DateTimeFormatter.ISO_LOCAL_TIME)
        .toFormatter()
private val ZonedDateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE)
        .optionalStart().appendLiteral('T').optionalEnd()
        .optionalStart().appendLiteral(' ').optionalEnd()
        .append(DateTimeFormatter.ISO_LOCAL_TIME)
        .appendPattern("X")
        .toFormatter()

fun ResultSet.getLocalDate(field: String) = LocalDate.from(
        DateTimeFormatter.ISO_LOCAL_DATE.parse(getString(field))
)!!

fun ResultSet.getLocalDateTime(field: String) = LocalDateTime.from(
        dateTimeFormatter.parse(getString(field))
)!!

fun ResultSet.getZonedDateTime(field: String) = ZonedDateTime.from(
        ZonedDateTimeFormatter.parse(getString(field))
)!!

fun ResultSet.getDuration(field: String) = Duration.parse(
        getString(field)
)!!

fun <T> ResultSet.getTypedArray(field: String) =
        getArray(field).array!! as Array<T>

fun <T> ResultSet.getJsonObject(
        field: String,
        objectMapper: ObjectMapper,
        arrayClass: Class<T>
): T {
    val arr= getString(field)
    return objectMapper.readValue(arr, arrayClass)
}
fun <T> ResultSet.getJsonObject(
        field: String,
        objectMapper: ObjectMapper,
        reference: TypeReference<T>
): T {
    val arr= getString(field)
    return objectMapper.readValue(arr, reference)
}