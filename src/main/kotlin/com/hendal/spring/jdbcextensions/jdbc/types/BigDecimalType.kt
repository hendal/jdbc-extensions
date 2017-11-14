package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import java.math.BigDecimal
import kotlin.reflect.KProperty1

class BigDecimalType <E, ID : Serializable>(
        columnName: String,
        val accesor: KProperty1<IEntity<E, ID>, BigDecimal>
) : BaseType<E,ID>(columnName) {
    override fun getParameter(entity: IEntity<E, ID>) = accesor.get(entity)
}