package com.hendal.spring.jdbcextensions

import org.springframework.context.support.ClassPathXmlApplicationContext

object AppConfig {
    fun getContext() =
            ClassPathXmlApplicationContext("context.xml")
}