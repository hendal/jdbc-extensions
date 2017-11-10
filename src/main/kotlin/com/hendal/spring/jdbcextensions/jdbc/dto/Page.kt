package com.hendal.spring.jdbcextensions.jdbc.dto

data class Page<out T>(val totalPages: Int, val totalItems: Long, val nextPage: Int, val backPage: Int, val items: List<T>){
}