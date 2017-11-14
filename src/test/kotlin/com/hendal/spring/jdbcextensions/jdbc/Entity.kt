package com.hendal.spring.jdbcextensions.jdbc

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class Entity(override val id: Long,
                  val my_date: LocalDate,
                  val my_timestamp: LocalDateTime,
                  val my_timestamp_time_zone: ZonedDateTime,
                  val my_text: String,
                  val my_int: Int,
                  val my_num: BigDecimal,
                  val my_array: Array<String>,
                  val my_json_array: String,
                  val my_json_obj: Any,
                  val my_interval: java.time.Duration,
                  val group: String
) : IEntity<Entity, Long> {
    override fun replicate(id: Long) = copy(id = id)
}