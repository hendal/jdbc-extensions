package com.hendal.spring.jdbcextensions.jdbc.impl

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.ResolverStyle
import java.time.temporal.Temporal
import java.util.*

object ParamInterpreter {
    fun interpretParams(map: Map<String, Any?>) = mapOf(
            *map.map(this::interpretParam).toTypedArray()
    )

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE!!
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME!!
    private val zonedDateTimeFormatter = (DateTimeFormatterBuilder())
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .optionalStart()
            .appendLiteral(' ')
            .parseCaseSensitive().appendZoneRegionId()
            .toFormatter()

    private fun interpretParam(map: Map.Entry<String, Any?>): Pair<String, Any?> {
        val (name, value) = map
        val newVal = when (value) {
            is String, is Number -> value
            is Temporal -> parse(value)
            is Duration -> value.toString()
            is Array<*> -> arrayToPostgresNotation(value)
            null -> null
            else -> throw RuntimeException("unrecognized Value Type ${value}")
        }
        return name to newVal
    }

    fun parse(date : Temporal) = when(date) {
        is LocalDateTime -> date.format(dateTimeFormatter)
        is LocalDate -> date.format(dateFormatter)
        is ZonedDateTime -> date.format(zonedDateTimeFormatter)
        else -> throw RuntimeException("unsuported Temporal ${date.javaClass.simpleName}")
    }

    fun arrayToPostgresNotation(arr: Array<*>): String {
        if (arr.isEmpty()) {
            return "{}"
        }
        val forType = arr[0]
        val newArr = when (forType) {
            is String -> (arr as Array<String>).map { quote(it) }
            is Number -> listOf(arr as Array<Number>)
            is LocalDate -> (arr as Array<LocalDate>).map { it.format(dateFormatter) }.map(::quote)
            is LocalDateTime -> (arr as Array<LocalDateTime>).map { it.format(dateTimeFormatter) }.map(::quote)
            is ZonedDateTime -> (arr as Array<ZonedDateTime>).map { it.format(zonedDateTimeFormatter) }.map(::quote)
            is Duration -> (arr as Array<Duration>).map { it.toString() }.map(::quote)
            else -> throw RuntimeException("error parsing array $forType")
        } as List<String>
        val content = java.lang.String.join(",", * newArr.toTypedArray())
        return "{$content}"
    }
}

private fun quote(it: String) = "'$it'"