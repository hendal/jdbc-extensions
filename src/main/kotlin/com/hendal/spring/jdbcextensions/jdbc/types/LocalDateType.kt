package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

class LocalDateType<E>(
        columnName: String,
        val accesor: KProperty1<E, LocalDate>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E): String =
            accesor.get(entity).format(DateTimeFormatter.ISO_DATE)
    override fun parameterForm(index: String) = "${super.parameterForm(index)}::date"

}