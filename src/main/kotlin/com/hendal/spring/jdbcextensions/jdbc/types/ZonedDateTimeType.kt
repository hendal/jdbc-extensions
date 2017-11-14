package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

class ZonedDateTimeType<E>(
        columnName: String,
        val accesor: KProperty1<E, ZonedDateTime>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E): String =
            accesor.get(entity).format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
}