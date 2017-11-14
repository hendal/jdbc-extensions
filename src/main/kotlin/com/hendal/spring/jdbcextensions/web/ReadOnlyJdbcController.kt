package com.hendal.spring.jdbcextensions.web

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.impl.ReadOnlyJdbcRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.io.Serializable

abstract class ReadOnlyJdbcController<T: IEntity<T,ID>, ID : Serializable> {

    abstract fun getService(): ReadOnlyJdbcRepository<T, ID>

    @GetMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun getById(@PathVariable id: ID) = getService().findOne(id)

    @GetMapping(produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun findAll() = getService().findAll()

    @GetMapping(params = arrayOf("page"))
    fun findPaged(@RequestParam(required = true) page: Int?,
                  @RequestParam(required = false) size: Int?,
                  @RequestParam(required = false) orderBy: Array<String>?) =
            getService().findAllPaginated(
                    page = page ?: 0,
                    size = size ?: 20,
                    sortColumns = orderBy ?: arrayOf(getService().id())
            )
}