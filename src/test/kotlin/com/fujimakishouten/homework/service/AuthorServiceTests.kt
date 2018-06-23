package com.fujimakishouten.homework.service

import com.fujimakishouten.homework.entity.AuthorEntity
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
class AuthorServiceTests {
    @Autowired
    lateinit var author: AuthorService

    @Test
    fun ShouldGetAllAuthors() {
        val result = author.findAll()

        assertThat(result).hasSize(3)
        assertThat(result[0].author_id).isEqualTo(1)
        assertThat(result[0].name).isEqualTo("高坂穂乃果")
        assertThat(result[1].author_id).isEqualTo(2)
        assertThat(result[1].name).isEqualTo("南ことり")
        assertThat(result[2].author_id).isEqualTo(3)
        assertThat(result[2].name).isEqualTo("園田海未")
    }

    @Test
    fun ShouldGetAuthorById() {
        val result = author.findById(1)
        assertThat(result).isNotNull()
        if (result is AuthorEntity) {
            assertThat(result.author_id).isEqualTo(1)
            assertThat(result.name).isEqualTo("高坂穂乃果")
        }
    }

    @Test
    fun ShouldGetNull() {
        val result = author.findById(0)
        assertThat(result).isNull()
    }

    @Test
    fun ShouldSaveAuthor() {
        val data = AuthorEntity()
        data.name = "西木野真姫"

        val result = author.save(data)
        assertThat(result.name).isEqualTo(data.name)

        author.delete(result)
    }

    @Test
    fun ShouldUpdateAuthor() {
        val data = AuthorEntity()
        val author_name = "小泉花陽"
        data.name = "星空凛"

        val from = author.save(data)
        assertThat(from.name).isEqualTo(data.name)

        from.name = author_name
        val result = author.save(from)
        assertThat(result.name).isEqualTo(author_name)

        author.delete(result)
    }

    @Test
    fun ShouldRemoveAuthor() {
        val data = AuthorEntity()
        data.name = "矢澤にこ"

        val saved = author.save(data)
        val author_id = saved.author_id
        assertThat(author_id).isNotNull()

        val deleted = author.delete(saved)
        assertThat(deleted).isEqualTo(kotlin.Unit)
        if (author_id != null) {
            val result = author.findById(author_id)
            assertThat(result).isNull()
        }
    }
}
