package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.impl.ParamInterpreter
import kotlin.reflect.KProperty1

class ArrayType<E, T>(
        columnName: String,
        val accesor: KProperty1<E, Array<T>>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E): String =
            ParamInterpreter.arrayToPostgresNotation(accesor.get(entity))
    override fun parameterForm(index: String) = "${super.parameterForm(index)}::text[]"
}