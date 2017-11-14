package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

class ZonedDateTimeType<E, ID : Serializable>(
        columnName: String,
        val accesor: KProperty1<IEntity<E, ID>, ZonedDateTime>
) : BaseType<E, ID>(columnName) {
    override fun getParameter(entity: IEntity<E, ID>): String =
            accesor.get(entity).format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
}