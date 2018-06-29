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
    fun ShouldGetSearchResult() {
        val allParameters = book.search("だよ", 1, 1)
        val titleOnly = book.search("だよ", null, null)
        val authorOnly = book.search(null, 1, null)
        val publisherOnly = book.search(null, null, 2)

        val authorAll = allParameters[0].author
        val publisherAll = allParameters[0].publisher
        assertThat(allParameters).hasSize(1)
        assertThat(allParameters[0].book_id).isEqualTo(1)
        assertThat(allParameters[0].title).isEqualTo("ファイトだよっ！")
        assertThat(allParameters[0].author_id).isEqualTo(1)
        assertThat(allParameters[0].publisher_id).isEqualTo(1)
        assertThat(allParameters[0].author).isNotNull()
        if (authorAll is AuthorEntity) {
            assertThat(authorAll.author_id).isEqualTo(allParameters[0].author_id)
            assertThat(authorAll.name).isEqualTo("高坂穂乃果")
        }
        assertThat(publisherAll).isNotNull()
        if (publisherAll is PublisherEntity) {
            assertThat(publisherAll.publisher_id).isEqualTo(allParameters[0].publisher_id)
            assertThat(publisherAll.name).isEqualTo("Printemps")
        }

        val authorTitleOnly = titleOnly[0].author
        val publisherTitleOnly = titleOnly[0].publisher
        assertThat(titleOnly).hasSize(1)
        assertThat(titleOnly[0].book_id).isEqualTo(1)
        assertThat(titleOnly[0].title).isEqualTo("ファイトだよっ！")
        assertThat(titleOnly[0].author_id).isEqualTo(1)
        assertThat(titleOnly[0].publisher_id).isEqualTo(1)
        assertThat(titleOnly[0].author).isNotNull()
        if (authorTitleOnly is AuthorEntity) {
            assertThat(authorTitleOnly.author_id).isEqualTo(titleOnly[0].author_id)
            assertThat(authorTitleOnly.name).isEqualTo("高坂穂乃果")
        }
        assertThat(publisherTitleOnly).isNotNull()
        if (publisherTitleOnly is PublisherEntity) {
            assertThat(publisherTitleOnly.publisher_id).isEqualTo(titleOnly[0].publisher_id)
            assertThat(publisherTitleOnly.name).isEqualTo("Printemps")
        }

        val authorAuthorOnly = authorOnly[0].author
        val publisherAuthoreOnly = authorOnly[0].publisher
        assertThat(authorOnly).hasSize(1)
        assertThat(authorOnly[0].book_id).isEqualTo(1)
        assertThat(authorOnly[0].title).isEqualTo("ファイトだよっ！")
        assertThat(authorOnly[0].author_id).isEqualTo(1)
        assertThat(authorOnly[0].publisher_id).isEqualTo(1)
        assertThat(authorOnly[0].author).isNotNull()
        if (authorAuthorOnly is AuthorEntity) {
            assertThat(authorAuthorOnly.author_id).isEqualTo(authorOnly[0].author_id)
            assertThat(authorAuthorOnly.name).isEqualTo("高坂穂乃果")
        }
        assertThat(publisherAuthoreOnly).isNotNull()
        if (publisherAuthoreOnly is PublisherEntity) {
            assertThat(publisherAuthoreOnly.publisher_id).isEqualTo(authorOnly[0].publisher_id)
            assertThat(publisherAuthoreOnly.name).isEqualTo("Printemps")
        }

        val authorPublisherOnly = publisherOnly[0].author
        val publisherPublisherOnly = publisherOnly[0].publisher
        assertThat(publisherOnly).hasSize(1)
        assertThat(publisherOnly[0].book_id).isEqualTo(3)
        assertThat(publisherOnly[0].title).isEqualTo("ラブアローシュート！")
        assertThat(publisherOnly[0].author_id).isEqualTo(3)
        assertThat(publisherOnly[0].publisher_id).isEqualTo(2)
        assertThat(publisherOnly[0].author).isNotNull()
        if (authorPublisherOnly is AuthorEntity) {
            assertThat(authorPublisherOnly.author_id).isEqualTo(publisherOnly[0].author_id)
            assertThat(authorPublisherOnly.name).isEqualTo("園田海未")
        }
        assertThat(publisherAuthoreOnly).isNotNull()
        if (publisherPublisherOnly is PublisherEntity) {
            assertThat(publisherPublisherOnly.publisher_id).isEqualTo(publisherOnly[0].publisher_id)
            assertThat(publisherPublisherOnly.name).isEqualTo("lily white")
        }
    }

    @Test
    fun ShouldNotGetSearchResult() {
        val titleUnmatch = book.search("サイトだよっ", null, null)
        val authorUnmatch = book.search(null, 2, 2)
        val publisherUnmatch = book.search(null, 1, 2)

        assertThat(titleUnmatch).hasSize(0)
        assertThat(authorUnmatch).hasSize(0)
        assertThat(publisherUnmatch).hasSize(0)
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
