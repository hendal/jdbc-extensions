package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import java.math.BigDecimal
import kotlin.reflect.KProperty1

class BigDecimalType <E>(
        columnName: String,
        val accesor: KProperty1<E, BigDecimal>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E) = accesor.get(entity)
}