package com.hendal.spring.jdbcextensions.jdbc.impl

import com.hendal.spring.jdbcextensions.AppConfig
import com.hendal.spring.jdbcextensions.jdbc.Entity
import org.junit.Before
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

@ContextConfiguration("/context.xml")
class JdbcRepositoryTest {

    lateinit var context: ApplicationContext
    lateinit var repo: EntityRepository
    var set = false

    @Before
    fun setup() {
        if (!set) {
            println("sleeping")
            Thread.sleep(5000)
            context = AppConfig.getContext()
            repo = context.getBean(EntityRepository::class.java)
            set = true
        }
        repo.getJdbcTemplate().update("TRUNCATE TABLE entity restart identity", emptyMap<String,Any>())

        repo.batchInsert(arrayOf(
                Entity(
                        id = -1,my_date = LocalDate.now(),my_timestamp = LocalDateTime.now(),
                        my_timestamp_time_zone = ZonedDateTime.now(),my_text = "my awesome,text",
                        my_int = 25,my_num = BigDecimal("3526.23"),my_array = arrayOf("p1","p2"),
                        group = "reserved word", my_interval = Duration.ofHours(3),
                        my_json_array = "[]" ,
                        my_json_obj = mapOf("esta" to "you","esa" to 25, "aquella" to null)
                )
        ))
    }

    @Test
    fun findAllTest() {
        println("cosas")
    }
}