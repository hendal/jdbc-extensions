package com.hendal.spring.jdbcextensions.jdbc.impl

import com.hendal.spring.jdbcextensions.AppConfig
import com.hendal.spring.jdbcextensions.jdbc.Entity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import java.math.BigDecimal
import java.time.*

@ContextConfiguration("/context.xml")
class JdbcRepositoryTest {

    lateinit var context: ApplicationContext
    lateinit var repo: EntityRepository
    var set = false
    val zone = ZoneId.of("America/Monterrey")!!
    val now =ZonedDateTime.now(zone)!!
    val entity1 = Entity(
            id = -1,my_date = LocalDate.now(),my_timestamp = LocalDateTime.now(),
            my_timestamp_time_zone = now,my_text = "my awesome,text",
            my_int = 25,my_num = BigDecimal("3526.23"),my_array = arrayOf("p1","p2"),
            group = "reserved word", my_interval = Duration.ofHours(3),
            my_json_array = arrayOf("my","not","empty","array") ,
            my_json_obj = mapOf("esta" to "you","esa" to 25, "aquella" to null)
    )

    @Before
    fun setup() {
        if (!set) {
            println("sleeping")
            Thread.sleep(10000)
            context = AppConfig.getContext()
            repo = context.getBean(EntityRepository::class.java)
            set = true
        }
        repo.getJdbcTemplate().update("TRUNCATE TABLE entity restart identity", emptyMap<String,Any>())

        repo.batchInsert(arrayOf(entity1))
    }

    @Test
    fun findAllTest() {
        val  entity= repo.findOne(1)
        val correctedModel = entity1.copy(id = 1)
        val correctedEntity = entity.copy(
                my_timestamp_time_zone = entity.my_timestamp_time_zone.withZoneSameInstant(zone),
                my_num = entity.my_num.setScale(2)
        )
        Assert.assertEquals(correctedModel, correctedEntity)
    }
}