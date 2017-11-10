package com.hendal.spring.jdbcextensions.web

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.JdbcRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.io.Serializable
import java.net.URI

abstract class JdbcController<T : IEntity<ID>, ID : Serializable> : ReadOnlyJdbcController<T, ID>() {
    abstract override fun getService(): JdbcRepository<T, ID>
    abstract fun basePath(): String

    @PostMapping(produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun insert(@RequestBody entity: T): ResponseEntity<Unit> {
        println("entrando")
        val id = getService().insert(entity)
        val headers = HttpHeaders()
        headers.location = URI("${basePath()}/$id")
        headers.contentType = MediaType.APPLICATION_JSON_UTF8
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun update(@RequestBody entity: T) = getService().update(entity)
}