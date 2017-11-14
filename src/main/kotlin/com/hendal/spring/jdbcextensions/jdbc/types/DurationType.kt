package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import java.time.Duration
import kotlin.reflect.KProperty1

class DurationType<E>(
        columnName: String,
        val accesor: KProperty1<E, Duration>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E): String =
            accesor.get(entity).toString()
}