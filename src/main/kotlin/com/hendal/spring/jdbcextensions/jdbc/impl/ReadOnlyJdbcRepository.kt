package com.hendal.spring.jdbcextensions.jdbc.impl

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.IReadOnlyRepository
import com.hendal.spring.jdbcextensions.jdbc.dto.Page
import com.hendal.spring.jdbcextensions.jdbc.types.BaseType
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.io.Serializable

abstract class ReadOnlyJdbcRepository<T : IEntity<T,ID>, ID : Serializable> : IReadOnlyRepository<T, ID> {
    abstract fun getJdbcTemplate(): NamedParameterJdbcTemplate
    abstract fun getters(): Map<String, BaseType<T>>
    fun getter(name: String): BaseType<T> = getters()[name]!!
    override fun findOne(
            id: ID,
            cols: Array<String>,
            rm: RowMapper<T>
    ): T {
        val params = mapOf(id() to id)

        return getJdbcTemplate().queryForObject(forgeSelect(
                cols = cols,
                page = -1,
                simpleWhere = params,
                size = -1,
                sortColumns = emptyArray()
        ), params, rm)!!
    }

    override fun findAll(
            simpleWhere: Map<String, Any>,
            cols: Array<String>,
            sortColumns: Array<String>,
            rm: RowMapper<T>
    ): List<T> {
        val parsedParams = ParamInterpreter.interpretParams(simpleWhere)
        return getJdbcTemplate().query(forgeSelect(
                cols = cols,
                page = -1,
                simpleWhere = parsedParams,
                size = -1,
                sortColumns = sortColumns
        ), rm)
    }

    override fun findAllPaginated(
            simpleWhere: Map<String, Any>,
            page: Int,
            size: Int,
            sortColumns: Array<String>,
            cols: Array<String>,
            rm: RowMapper<T>
    ): Page<T> {
        val parsedParams = ParamInterpreter.interpretParams(simpleWhere)
        val total = getJdbcTemplate().queryForObject(forgeSelect(
                cols = arrayOf("count(1)"),
                page = -1,
                simpleWhere = parsedParams,
                size = -1,
                sortColumns = arrayOf()
        ), parsedParams, Long::class.java)!!
        val items = if (total != 0L && total > size * page) {
            getJdbcTemplate().query(forgeSelect(
                    cols = cols,
                    page = page,
                    simpleWhere = parsedParams,
                    size = size,
                    sortColumns = sortColumns
            ), simpleWhere, rm)
        } else {
            emptyList<T>()
        }
        val totalPages = Math.ceil(total / size.toDouble()).toInt()
        val backPage = if (page - 1 < 0) 0 else page - 1
        val nextPage = if (page + 1 >= totalPages) totalPages - 1 else page + 1
        return Page(totalPages = totalPages,
                totalItems = total,
                nextPage = nextPage,
                backPage = backPage,
                items = items)
    }

    private fun forgeSelect(cols: Array<String>, simpleWhere: Map<String, *>, page: Int, size: Int, sortColumns: Array<String>): String {
        val selectColumns = cols.map(this::getter).map { it.columnName() } .toTypedArray()
        val columns = java.lang.String.join(",", *selectColumns)!!
        val sColumns = sortColumns.map(this::getter).map { it.columnName() } .toTypedArray()
        val sortC = java.lang.String.join(",", *sColumns)!!
        val sort = if (sortC.isEmpty()) "" else "ORDER BY $sortC"
        val where = simpleWhereInterpret(simpleWhere)
        val paging = (if (size != -1) " LIMIT $size " else "") + (if (page != -1) "OFFSET ${page * size}" else "")
        return "SELECT $columns FROM ${tableName()} $where $sort $paging"
    }

    private fun simpleWhereInterpret(where: Map<String, *>): String {
        val pieces = where.entries.map(this::simpleWhereRuleInterpret)
        val result = java.lang.String.join(" AND ", pieces)
        return if (result.isEmpty()) "" else "WHERE $result"
    }

    private fun simpleWhereRuleInterpret(param: Map.Entry<String, *>): String {
        val (key, value) = param
        if (key.contains(";") || key.contains("'")) {
            throw RuntimeException("$key no es un nombre de columna vaido")
        }
        val returnVal: String
        val paramName = extractColumnName(key)
        returnVal = when (value) {
            is Array<*>, is List<*> -> "$key = IN (:$paramName)"
            else -> "$key = :$paramName"
        }
        return returnVal
    }

    private val functionRegex = Regex("([a-z_0-9]*)\\(([a-z_0-9])\\)")
    private fun extractColumnName(key: String): String {
        if (key.contains("\\(")) {
            return functionRegex.replace(key, "$2")
        }
        return key
    }
}