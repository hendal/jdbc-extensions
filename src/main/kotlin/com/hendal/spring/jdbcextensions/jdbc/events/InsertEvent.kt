package com.hendal.spring.jdbcextensions.jdbc.events

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.IRepository
import org.springframework.context.ApplicationEvent
import java.io.Serializable

class InsertEvent<T : IEntity<T,ID>, ID : Serializable>(
        iRepository: IRepository<T, ID>,
        entity: T
) : RepositoryEvent<T, ID>(iRepository, entity) {
    override val insert = true
}