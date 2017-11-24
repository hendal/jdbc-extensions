package com.hendal.spring.jdbcextensions.jdbc.types

abstract class BaseType<E>(val columnName: String) {
    open fun columnName() = "\"$columnName\""
    open fun parameterForm(index: String = "") = ":$columnName$index"
    override fun toString() = "${javaClass.simpleName}:$columnName"
    abstract fun getParameter(entity: E): Any?
}