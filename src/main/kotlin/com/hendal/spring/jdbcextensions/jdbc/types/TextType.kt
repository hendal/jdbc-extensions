package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable
import kotlin.reflect.KProperty1

class TextType<E, ID : Serializable>(
        columnName: String,
        val accesor: KProperty1<IEntity<E, ID>, String>
) : BaseType<E,ID>(columnName) {
    override fun getParameter(entity: IEntity<E, ID>): String = accesor.get(entity)
}