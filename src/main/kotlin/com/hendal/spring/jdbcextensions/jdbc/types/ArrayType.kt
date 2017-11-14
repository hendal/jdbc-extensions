package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.impl.ParamInterpreter
import java.io.Serializable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

class ArrayType<E, ID : Serializable, T>(
        columnName: String,
        val accesor: KProperty1<IEntity<E, ID>, Array<T>>
) : BaseType<E, ID>(columnName) {
    override fun getParameter(entity: IEntity<E, ID>): String =
            ParamInterpreter.arrayToPostgresNotation(accesor.get(entity))
}