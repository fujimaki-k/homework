package com.fujimakishouten.homework.service

import com.fujimakishouten.homework.entity.AuthorEntity
import com.fujimakishouten.homework.entity.BookEntity
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
class BookServiceTests {
    @Autowired
    lateinit var book: BookService

    @Test
    fun ShouldGetAllBooks() {
        val result = book.findAll()

        val author1 = result[0].author
        val publisher1 = result[0].publisher
        assertThat(result).hasSize(3)
        assertThat(result[0].book_id).isEqualTo(1)
        assertThat(result[0].title).isEqualTo("ファイトだよっ！")
        assertThat(result[0].author_id).isEqualTo(1)
        assertThat(result[0].publisher_id).isEqualTo(1)
        assertThat(author1).isNotNull()
        if (author1 is AuthorEntity) {
            assertThat(author1.author_id).isEqualTo(result[0].author_id)
            assertThat(author1.name).isEqualTo("高坂穂乃果")
        }
        assertThat(publisher1).isNotNull()
        if (publisher1 is PublisherEntity) {
            assertThat(publisher1.publisher_id).isEqualTo(result[0].publisher_id)
            assertThat(publisher1.name).isEqualTo("Printemps")
        }

        val author2 = result[1].author
        val publisher2 = result[1].publisher
        assertThat(result[1].book_id).isEqualTo(2)
        assertThat(result[1].title).isEqualTo("（・８・）")
        assertThat(result[1].author_id).isEqualTo(2)
        assertThat(result[1].publisher_id).isEqualTo(1)
        assertThat(author2).isNotNull()
        if (author2 is AuthorEntity) {
            assertThat(author2.author_id).isEqualTo(result[1].author_id)
            assertThat(author2.name).isEqualTo("南ことり")
        }
        assertThat(publisher2).isNotNull()
        if (publisher2 is PublisherEntity) {
            assertThat(publisher2.publisher_id).isEqualTo(result[1].publisher_id)
            assertThat(publisher2.name).isEqualTo("Printemps")
        }


        val author3 = result[2].author
        val publisher3 = result[2].publisher
        assertThat(result[2].book_id).isEqualTo(3)
        assertThat(result[2].title).isEqualTo("ラブアローシュート！")
        assertThat(result[2].author_id).isEqualTo(3)
        assertThat(result[2].publisher_id).isEqualTo(2)
        assertThat(author1).isNotNull()
        if (author3 is AuthorEntity) {
            assertThat(author3.author_id).isEqualTo(result[2].author_id)
            assertThat(author3.name).isEqualTo("園田海未")
        }
        assertThat(publisher3).isNotNull()
        if (publisher3 is PublisherEntity) {
            assertThat(publisher3.publisher_id).isEqualTo(result[2].publisher_id)
            assertThat(publisher3.name).isEqualTo("lily white")
        }
    }

    @Test
    fun ShouldGetBooksByTitle() {
        val result = book.findByTitle("だよ")

        val author = result[0].author
        val publisher = result[0].publisher
        assertThat(result).hasSize(1)
        assertThat(result[0].book_id).isEqualTo(1)
        assertThat(result[0].title).isEqualTo("ファイトだよっ！")
        assertThat(result[0].author_id).isEqualTo(1)
        assertThat(result[0].publisher_id).isEqualTo(1)
        assertThat(author).isNotNull()
        if (author is AuthorEntity) {
            assertThat(author.author_id).isEqualTo(result[0].author_id)
            assertThat(author.name).isEqualTo("高坂穂乃果")
        }
        assertThat(publisher).isNotNull()
        if (publisher is PublisherEntity) {
            assertThat(publisher.publisher_id).isEqualTo(result[0].publisher_id)
            assertThat(publisher.name).isEqualTo("Printemps")
        }
    }

    @Test
    fun ShouldGetBookById() {
        val result = book.findById(1)

        assertThat(result).isNotNull()
        if (result is BookEntity) {
            val author = result.author
            val publisher = result.publisher
            assertThat(result.book_id).isEqualTo(1)
            assertThat(result.title).isEqualTo("ファイトだよっ！")
            assertThat(result.author_id).isEqualTo(1)
            assertThat(result.publisher_id).isEqualTo(1)
            if (author is AuthorEntity) {
                assertThat(author.author_id).isEqualTo(result.author_id)
                assertThat(author.name).isEqualTo("高坂穂乃果")
            }
            assertThat(publisher).isNotNull()
            if (publisher is PublisherEntity) {
                assertThat(publisher.publisher_id).isEqualTo(result.publisher_id)
                assertThat(publisher.name).isEqualTo("Printemps")
            }
        }
    }

    @Test
    fun ShouldGetNull() {
        val result = book.findById(0)
        assertThat(result).isNull()
    }

    @Test
    fun ShouldSaveBook() {
        val data = BookEntity(
            title = "あんこ飽きた",
            author_id = 1,
            publisher_id = 1
        )
        val result = book.save(data)
        assertThat(result.title).isEqualTo(data.title)
        assertThat(result.author_id).isEqualTo(data.author_id)
        assertThat(result.publisher_id).isEqualTo(data.publisher_id)

        book.delete(result)
    }

    @Test
    fun ShouldUpdateBook() {
        val data = BookEntity(
            title = "やるったらやる！",
            author_id = 1,
            publisher_id = 1
        )
        val book_title = "やり遂げたよ。最後までーー！"
        val from = book.save(data)
        assertThat(from.title).isEqualTo(data.title)
        assertThat(from.author_id).isEqualTo(data.author_id)
        assertThat(from.publisher_id).isEqualTo(data.publisher_id)

        from.title = book_title
        val result = book.save(from)
        assertThat(result.title).isEqualTo(book_title)
        assertThat(result.author_id).isEqualTo(result.author_id)
        assertThat(result.publisher_id).isEqualTo(result.publisher_id)

        book.delete(result)
    }

    @Test
    fun ShouldRemovePublisher() {
        val data = BookEntity(
            title = "あなたは最低です",
            author_id = 3,
            publisher_id = 2
        )
        val saved = book.save(data)
        val book_id = saved.book_id
        assertThat(book_id).isNotNull()

        val deleted = book.delete(saved)
        assertThat(deleted).isEqualTo(kotlin.Unit)
        if (book_id != null) {
            val result = book.findById(book_id)
            assertThat(result).isNull()
        }
    }
}
