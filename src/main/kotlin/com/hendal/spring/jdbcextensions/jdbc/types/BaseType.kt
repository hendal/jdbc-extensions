package com.hendal.spring.jdbcextensions.jdbc.types

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import java.io.Serializable

abstract class BaseType<E, ID : Serializable>(val columnName: String) {
    fun columnName() = "\"$columnName\""
    fun parameterForm(index: String = "") = ":$columnName$index"
    override fun toString() = "${javaClass.simpleName}:$columnName"
    abstract fun getParameter(entity: IEntity<E, ID>): Any?
}