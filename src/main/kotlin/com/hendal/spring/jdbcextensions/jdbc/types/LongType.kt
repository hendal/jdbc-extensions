package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import kotlin.reflect.KProperty1

class LongType<E>(
        columnName: String,
        val accesor: KProperty1<E, Long>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E): Long = accesor.get(entity)
}