package com.hendal.spring.jdbcextensions.jdbc

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

data class Entity(override val id: Long,
                  val my_date: LocalDate,
                  val my_timestamp: LocalDateTime,
                  val my_timestamp_time_zone: ZonedDateTime,
                  val my_text: String,
                  val my_int: Int,
                  val my_num: BigDecimal,
                  val my_array: Array<String>,
                  val my_json_array: Array<String>,
                  val my_json_obj: Map<String,Any?>,
                  val my_interval: java.time.Duration,
                  val group: String
) : IEntity<Entity, Long> {
    override fun replicate(id: Long) = copy(id = id)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Entity

        if (id != other.id) return false
        if (my_date != other.my_date) return false
        if (my_timestamp != other.my_timestamp) return false
        if (my_timestamp_time_zone != other.my_timestamp_time_zone) return false
        if (my_text != other.my_text) return false
        if (my_int != other.my_int) return false
        if (my_num != other.my_num) return false
        if (!Arrays.equals(my_array, other.my_array)) return false
        if (!Arrays.equals(my_json_array, other.my_json_array)) return false
//        Map equals not working correctly
//        if (my_json_obj != other.my_json_obj) return false
        if (my_interval != other.my_interval) return false
        if (group != other.group) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + my_date.hashCode()
        result = 31 * result + my_timestamp.hashCode()
        result = 31 * result + my_timestamp_time_zone.hashCode()
        result = 31 * result + my_text.hashCode()
        result = 31 * result + my_int
        result = 31 * result + my_num.hashCode()
        result = 31 * result + Arrays.hashCode(my_array)
        result = 31 * result + Arrays.hashCode(my_json_array)
        result = 31 * result + my_json_obj.hashCode()
        result = 31 * result + my_interval.hashCode()
        result = 31 * result + group.hashCode()
        return result
    }
}

data class SubEntityJsonObject(
        val name:String,
        val desc:String
)