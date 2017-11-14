package com.hendal.spring.jdbcextensions.jdbc.impl

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object ParamInterpreter {
    fun interpretParams(map: Map<String, Any?>) = mapOf(
            *map.map(this::interpretParam).toTypedArray()
    )

    val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE!!
    val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME!!
    val zonedDateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME!!

    private fun interpretParam(map: Map.Entry<String, Any?>): Pair<String, Any?> {
        val (name, value) = map
        val newVal = when (value) {
            is String, is Number -> value
            is LocalDateTime -> value.format(dateTimeFormatter)
            is LocalDate -> value.format(dateFormatter)
            is ZonedDateTime -> value.format(zonedDateTimeFormatter)
            is Duration -> value.toString()
            is Array<*> -> arrayToPostgresNotation(value)
            null -> null
            else -> throw RuntimeException("unrecognized Value Type ${value}")
        }
        return name to newVal
    }

    fun arrayToPostgresNotation(arr: Array<*>): String {
        if (arr.isEmpty()) {
            return "{}"
        }
        val forType = arr[0]
        val newArr = when (forType) {
            is String -> (arr as Array<String>).map(::quote)
            is Number -> arr
            is LocalDate -> (arr as Array<LocalDate>).map { it.format(dateFormatter) }.map(::quote)
            is LocalDateTime -> (arr as Array<LocalDateTime>).map { it.format(dateTimeFormatter) }.map(::quote)
            is ZonedDateTime -> (arr as Array<ZonedDateTime>).map { it.format(zonedDateTimeFormatter) }.map(::quote)
            is Duration -> (arr as Array<Duration>).map { it.toString() }.map(::quote)
            else -> throw RuntimeException("error parsing array $forType")
        } as Array<String>
        val content = java.lang.String.join(",", * newArr)
        return "{$content}"
    }
}

private fun quote(it: String) = "'$it'"