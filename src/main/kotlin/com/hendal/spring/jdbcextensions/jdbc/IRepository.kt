package com.hendal.spring.jdbcextensions.jdbc

import java.io.Serializable

interface IRepository<T : IEntity<T,ID>, ID : Serializable> : IReadOnlyRepository<T, ID> {
    fun insert(entity: T): ID

    fun delete(entity: T)

    fun batchInsert(entities: Array<T>, columns: Array<String> = columns())

    fun update(entity: T, columns: Array<String> = columns())
}