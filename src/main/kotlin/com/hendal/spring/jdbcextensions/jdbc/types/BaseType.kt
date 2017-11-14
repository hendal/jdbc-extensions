package com.hendal.spring.jdbcextensions.jdbc.types

abstract class BaseType<E>(val columnName: String) {
    fun columnName() = "\"$columnName\""
    fun parameterForm(index: String = "") = ":$columnName$index"
    override fun toString() = "${javaClass.simpleName}:$columnName"
    abstract fun getParameter(entity: E): Any?
}