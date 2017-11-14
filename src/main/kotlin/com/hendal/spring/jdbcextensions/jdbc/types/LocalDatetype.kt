package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

class LocalDatetype<E, ID : Serializable>(
        columnName: String,
        val accesor: KProperty1<IEntity<E, ID>, LocalDate>
) : BaseType<E, ID>(columnName) {
    override fun getParameter(entity: IEntity<E, ID>): String =
            accesor.get(entity).format(DateTimeFormatter.ISO_DATE)
}