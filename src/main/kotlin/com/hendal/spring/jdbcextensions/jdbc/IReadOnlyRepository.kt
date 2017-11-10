package com.hendal.spring.jdbcextensions.jdbc

import com.hendal.spring.jdbcextensions.jdbc.dto.Page
import org.springframework.jdbc.core.RowMapper
import java.io.Serializable

interface IReadOnlyRepository<T, in ID : Serializable> {

    fun tableName(): String
    fun columns(): Array<String>
    fun id(): String
    fun rowMapper(): RowMapper<T>

    fun findOne(id: ID,
                cols: Array<String> = columns(),
                rm: RowMapper<T> = rowMapper()
    ): T

    fun findAll(
            simpleWhere: Map<String, *> = mapOf<String, Any>(),
            cols: Array<String> = columns(),
            sortColumns: Array<String> = arrayOf(id()),
            rm: RowMapper<T> = rowMapper()
    ): List<T>

    fun findAllPaginated(
            simpleWhere: Map<String, *> = mapOf<String, Any>(),
            page: Int,
            size: Int,
            sortColumns: Array<String> = arrayOf(id()),
            cols: Array<String> = columns(),
            rm: RowMapper<T> = rowMapper()
    ): Page<T>

}