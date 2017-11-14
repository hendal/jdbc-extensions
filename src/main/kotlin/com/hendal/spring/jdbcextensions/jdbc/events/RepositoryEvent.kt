package com.hendal.spring.jdbcextensions.jdbc.events

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import com.hendal.spring.jdbcextensions.jdbc.IRepository
import org.springframework.context.ApplicationEvent
import java.io.Serializable

abstract class RepositoryEvent<T : IEntity<T,ID>, ID : Serializable>(
        private val iRepository: IRepository<T, ID>,
        val entity: IEntity<T,ID>
) : ApplicationEvent(iRepository) {
    override fun getSource() = iRepository
    abstract val insert: Boolean
    open val delete = false
}