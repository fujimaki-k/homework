package com.fujimakishouten.homework.service

import com.fujimakishouten.homework.entity.PublisherEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
class PublisherServiceTests {
    @Autowired
    lateinit var publisher: PublisherService

    @Test
    fun ShouldGetAllPublishers() {
        val result = publisher.findAll()

        assertThat(result).hasSize(2)
        assertThat(result[0].publisher_id).isEqualTo(1)
        assertThat(result[0].name).isEqualTo("Printemps")
        assertThat(result[1].publisher_id).isEqualTo(2)
        assertThat(result[1].name).isEqualTo("lily white")
    }

    @Test
    fun ShouldGetPublisherById() {
        val result = publisher.findById(1)
        assertThat(result).isNotNull()
        if (result is PublisherEntity) {
            assertThat(result.publisher_id).isEqualTo(1)
            assertThat(result.name).isEqualTo("Printemps")
        }
    }

    @Test
    fun ShouldGetNull() {
        val result = publisher.findById(0)
        assertThat(result).isNull()
    }

    @Test
    fun ShouldSavePublisher() {
        val data = PublisherEntity()
        data.name = "BiBi"

        val result = publisher.save(data)
        assertThat(result.name).isEqualTo(data.name)

        publisher.delete(result)
    }

    @Test
    fun ShouldUpdatePublisher() {
        val data = PublisherEntity()
        val publisher_name = "BiBi"
        data.name = "μ'ｓ"

        val from = publisher.save(data)
        assertThat(from.name).isEqualTo(data.name)

        from.name = publisher_name
        val result = publisher.save(from)
        assertThat(result.name).isEqualTo(publisher_name)

        publisher.delete(result)
    }

    @Test
    fun ShouldRemovePublisher() {
        val data = PublisherEntity()
        data.name = "BiBi"

        val saved = publisher.save(data)
        val publisher_id = saved.publisher_id
        assertThat(publisher_id).isNotNull()

        val deleted = publisher.delete(saved)
        assertThat(deleted).isEqualTo(kotlin.Unit)
        if (publisher_id != null) {
            val result = publisher.findById(publisher_id)
            assertThat(result).isNull()
        }
    }
}
