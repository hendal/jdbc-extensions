package com.hendal.spring.jdbcextensions.jdbc

import java.io.Serializable

interface IEntity<out SELF,ID : Serializable> {
    val id : ID
    fun replicate(id : ID) : SELF
}