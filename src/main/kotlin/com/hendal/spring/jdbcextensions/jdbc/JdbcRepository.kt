package com.hendal.spring.jdbcextensions.jdbc

import org.springframework.context.ApplicationEventPublisher
import java.io.Serializable
import kotlin.reflect.KProperty1

abstract class JdbcRepository<T : IEntity<ID>, ID : Serializable>(private val eventPublisher: ApplicationEventPublisher) : ReadOnlyJdbcRepository<T, ID>(), IRepository<T, ID> {

    abstract fun getters(): Map<String, KProperty1<T, *>>

    override fun columns(): Array<String> = getters().keys.toTypedArray()

    fun getter(name: String): KProperty1<T, *> = getters()[name]!!
    fun save(entity: T) {
        val id = getter(id()).get(entity)
        when (id) {
            is Long -> if (id == -1L) insert(entity) else update(entity)
            else -> if (id == null) insert(entity) else update(entity)
        }
    }

    override fun insert(entity: T): ID {
        val params = entityToParams(entity)
        val id = getJdbcTemplate().execute(forgeInsert(entity), params) {
            it.executeQuery().let {
                @Suppress("UNCHECKED_CAST")
                if (it.next()) it.getObject(1) as ID else throw RuntimeException("no ID")
            }
        }!!
        return id
    }

    override fun delete(entity: T) {
        val params = mapOf("id" to getter(id()).get(entity))
        getJdbcTemplate().update("DELETE FROM ${tableName()} WHERE ${id()} = :id", params)
    }

    override fun batchInsert(entities: Array<T>, columns: Array<String>) {
        if (entities.isNotEmpty()) {
            val params = entities.map { entityToParams(it, columns) }.toTypedArray()
            getJdbcTemplate().batchUpdate(forgeInsert(entities[0]), params)
        }
    }

    fun entityToParams(entity: T, cols: Array<String> = columns(), insert: Boolean = true): Map<String, Any?> {
        val pairs = if (insert) {
            cols.filter { id() != it }.map { it to getter(it).get(entity) }
                    .filter { it.second != null }
                    .toTypedArray()
        } else {
            cols.map { it to getter(it).get(entity) }
                    .filter { it.second != null }
                    .toTypedArray()
        }
        return mapOf(*pairs)
    }

    fun forgeInsert(entity: T, columns: Array<String> = columns()): String {
        val cols = columns.filter { !id().equals(it) }.filter {
            getter(it).get(entity) != null
        }
        val insertColumns = java.lang.String.join(",", cols)
        val insertParameters = ":" + insertColumns.replace(",", ",:")
        return "INSERT INTO ${tableName()}($insertColumns) VALUES ($insertParameters) RETURNING ${id()}"
    }

    override fun update(entity: T, columns: Array<String>) {
        val params = entityToParams(entity, columns, false)
        getJdbcTemplate().update(forgeUpdate(columns), params)
    }

    fun forgeUpdate(columns: Array<String> = columns()): String {
        val cols = columns.filter { id() != it }.map { "$it = :$it" }
        val sets = java.lang.String.join(", ", cols)
        return "UPDATE ${tableName()} SET $sets WHERE ${id()} = :${id()}"
    }
}