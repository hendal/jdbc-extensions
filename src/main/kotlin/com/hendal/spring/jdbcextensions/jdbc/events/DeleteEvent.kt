package com.hendal.spring.jdbcextensions.jdbc.events

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.IRepository
import java.io.Serializable

class DeleteEvent<T : IEntity<T, ID>, ID : Serializable>(
        iRepository: IRepository<T, ID>,
        entity: T
) : RepositoryEvent<T, ID>(iRepository, entity) {
    override val insert = false
    override val delete = true
}