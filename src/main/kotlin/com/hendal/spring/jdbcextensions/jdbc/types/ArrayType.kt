package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.impl.ParamInterpreter
import java.io.Serializable
import kotlin.reflect.KProperty1

class ArrayType<E, ID : Serializable, T>(
        columnName: String,
        val accesor: KProperty1<E, Array<T>>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E): String =
            ParamInterpreter.arrayToPostgresNotation(accesor.get(entity))
}