package com.hendal.spring.jdbcextensions.jdbc.events

import com.hendal.spring.jdbcextensions.jdbc.IEntity
import org.springframework.context.ApplicationEvent
import java.io.Serializable

class InsertEvent<T : IEntity<ID>, ID : Serializable>(entity: T) : ApplicationEvent(entity)