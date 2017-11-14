package com.hendal.spring.jdbcextensions.jdbc.impl

import com.hendal.spring.jdbcextensions.jdbc.*
import com.hendal.spring.jdbcextensions.jdbc.types.LongType
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class EntityRepository(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : JdbcRepository<Entity, Long>() {
    override fun getters() = mapOf(
            id() to LongType(id(),Entity::id),
            "my_date" to Entity::my_date,
            "my_timestamp" to Entity::my_timestamp,
            "my_timestamp_time_zone" to Entity::my_timestamp_time_zone,
            "my_text" to Entity::my_text,
            "my_int" to Entity::my_int,
            "my_num" to Entity::my_num,
            "my_array" to Entity::my_array,
            "my_json_array" to Entity::my_json_array,
            "my_json_obj" to Entity::my_json_obj,
            "my_interval" to Entity::my_interval,
            "group" to Entity::group
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
                my_json_array = rs.getString("my_json_array"),
                my_json_obj= rs.getString("my_json_obj"),
                my_interval= rs.getDuration("my_interval"),
                group = rs.getString("group")
        )
    }

    override fun getJdbcTemplate() = namedParameterJdbcTemplate

    override fun tableName() = "entity"

    override fun id() = "entity_id"
}