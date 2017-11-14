package com.hendal.spring.jdbcextensions.jdbc.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.hendal.spring.jdbcextensions.jdbc.*
import com.hendal.spring.jdbcextensions.jdbc.types.*
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class EntityRepository(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
        private val objectMapper: ObjectMapper
) : JdbcRepository<Entity, Long>() {
    override fun getters() = mapOf(
            id() to LongType(id(), Entity::id),
            "my_date" to LocalDateType("my_date", Entity::my_date),
            "my_timestamp" to LocalDateTimeType("my_timestamp", Entity::my_timestamp),
            "my_timestamp_time_zone" to ZonedDateTimeType("my_timestamp_time_zone", Entity::my_timestamp_time_zone),
            "my_text" to TextType("my_text",Entity::my_text),
            "my_int" to IntType("my_int",Entity::my_int),
            "my_num" to BigDecimalType("my_num",Entity::my_num),
            "my_array" to ArrayType("my_array",Entity::my_array),
            "my_json_array" to JsonArrayType("my_json_array",Entity::my_json_array,objectMapper),
            "my_json_obj" to JsonObjectType("my_json_obj",Entity::my_json_obj,objectMapper),
            "my_interval" to DurationType("my_interval",Entity::my_interval),
            "group" to TextType("group",Entity::group)
    )

    override fun rowMapper() = RowMapper { rs, _ ->
        Entity(
                id = rs.getLong(id()),
                my_date = rs.getLocalDate("my_date"),
                my_timestamp = rs.getLocalDateTime("my_timestamp"),
                my_timestamp_time_zone = rs.getZonedDateTime("my_timestamp_time_zone"),
                my_text = rs.getString("my_text"),
                my_int = rs.getInt("my_int"),
                my_num = rs.getBigDecimal("my_num"),
                my_array = rs.getTypedArray("my_array"),
                my_json_array = rs.getTypedArray("my_json_array"),
                my_json_obj = rs.getString("my_json_obj"),
                my_interval = rs.getDuration("my_interval"),
                group = rs.getString("group")
        )
    }

    override fun getJdbcTemplate() = namedParameterJdbcTemplate

    override fun tableName() = "entity"

    override fun id() = "entity_id"
}