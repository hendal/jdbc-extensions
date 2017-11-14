package com.hendal.spring.jdbcextensions.jdbc.impl

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.IRepository
import com.hendal.spring.jdbcextensions.jdbc.events.InsertEvent
import com.hendal.spring.jdbcextensions.jdbc.events.UpdateEvent
import com.hendal.spring.jdbcextensions.jdbc.types.BaseType
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationEventPublisher
import java.io.Serializable

abstract class JdbcRepository<T : IEntity<T, ID>, ID : Serializable> : ReadOnlyJdbcRepository<T, ID>(), IRepository<T, ID>, ApplicationContextAware {

    lateinit var eventPublisher: ApplicationEventPublisher
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.eventPublisher = applicationContext
    }

    abstract fun getters(): Map<String, BaseType<T>>

    override fun columns(): Array<String> = getters().keys.toTypedArray()

    fun getter(name: String): BaseType<T> = getters()[name]!!
    fun save(entity: T) {
        val id = getter(id()).getParameter(entity)
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
        eventPublisher.publishEvent(InsertEvent(this, entity))
        return id
    }

    override fun delete(entity: T) {
        val params = mapOf("id" to getter(id()).getParameter(entity))
        getJdbcTemplate().update("DELETE FROM ${tableName()} WHERE ${id()} = :id", params)
    }

    override fun batchInsert(entities: Array<T>, columns: Array<String>) {
        if (entities.isNotEmpty()) {
            val params = entities.map { entityToParams(it, columns) }.toTypedArray()
            val insert = forgeInsert(entities[0])
            getJdbcTemplate().batchUpdate(insert, params)
        }
    }

    private fun entityToParams(entity: T, cols: Array<String> = columns(), insert: Boolean = true): Map<String, Any?> {
        val pairs = if (insert) {
            cols.filter { id() != it }.map { it to getter(it).getParameter(entity) }
                    .filter { it.second != null }
                    .toTypedArray()
        } else {
            cols.map { it to getter(it).getParameter(entity) }
                    .filter { it.second != null }
                    .toTypedArray()
        }
        return ParamInterpreter.interpretParams(mapOf(*pairs))
    }

    private fun forgeInsert(entity: T, columns: Array<String> = columns()): String {
        val cols = columns.filter { !id().equals(it) }.filter {
            getter(it).getParameter(entity) != null
        }
        val insertColumns = java.lang.String.join(",", cols.map { getter(it).columnName() })
        val insertParameters = java.lang.String.join(",",cols.map { getter(it).parameterForm() })
        return "INSERT INTO ${tableName()}($insertColumns) VALUES ($insertParameters) RETURNING ${id()}"
    }

    override fun update(entity: T, columns: Array<String>) {
        val params = entityToParams(entity, columns, false)
        getJdbcTemplate().update(forgeUpdate(columns), params)
        eventPublisher.publishEvent(UpdateEvent(this, entity))
    }

    private fun forgeUpdate(columns: Array<String> = columns()): String {
        val cols = columns.filter { id() != it }.map { "$it = :$it" }
        val sets = java.lang.String.join(", ", cols)
        return "UPDATE ${tableName()} SET $sets WHERE ${id()} = :${id()}"
    }
}