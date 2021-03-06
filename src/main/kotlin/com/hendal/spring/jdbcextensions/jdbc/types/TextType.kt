package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import kotlin.reflect.KProperty1

class TextType<E>(
        columnName: String,
        val accesor: KProperty1<E, String>
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E): String = accesor.get(entity)
}