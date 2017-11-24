package com.hendal.spring.jdbcextensions.jdbc.types

class FunctionType<E>(
        val func: String
) : BaseType<E>(func) {
    override fun columnName(): String = func
    override fun parameterForm(index: String): String = func
    override fun getParameter(entity: E): Any? = null
}