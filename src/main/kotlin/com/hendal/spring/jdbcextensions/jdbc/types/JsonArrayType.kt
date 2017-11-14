package com.hendal.spring.jdbcextensions.jdbc.types

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KProperty1

class JsonArrayType<E, O>(
        columnName: String,
        val accesor: KProperty1<E, Array<O>>,
        private val objectMapper: ObjectMapper
) : BaseType<E>(columnName) {
    override fun getParameter(entity: E) : String =
            objectMapper.writeValueAsString(accesor.get(entity))
    override fun parameterForm(index: String) = ":$columnName$index::jsonb"
}