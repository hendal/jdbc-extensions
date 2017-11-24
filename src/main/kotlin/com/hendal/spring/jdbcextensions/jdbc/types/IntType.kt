package com.hendal.spring.jdbcextensions.jdbc.types

import kotlin.reflect.KProperty1

class IntType<E>(
        columnName: String,
        val accesor: KProperty1<E, Int>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E): Int = accesor.get(entity)
}