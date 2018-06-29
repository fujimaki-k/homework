package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.service.BookService
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookControllerTests {

    @Autowired
    lateinit var mock: MockMvc

    @Autowired
    lateinit var book: BookService

    @Test
    fun ShouldGetBookList() {
        val url = "/book"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")

        assertThat(trElements).hasSize(3)
        val tdElements1 = trElements[0].select("td")
        assertThat(tdElements1[0].text()).isEqualTo("1")
        assertThat(tdElements1[1].text()).isEqualTo("ファイトだよっ！")
        assertThat(tdElements1[2].text()).isEqualTo("高坂穂乃果")
        assertThat(tdElements1[3].text()).isEqualTo("Printemps")
        assertThat(tdElements1[4].text()).isEqualTo("削除する")

        val tdElements2 = trElements[1].select("td")
        assertThat(tdElements2[0].text()).isEqualTo("2")
        assertThat(tdElements2[1].text()).isEqualTo("（・８・）")
        assertThat(tdElements2[2].text()).isEqualTo("南ことり")
        assertThat(tdElements2[3].text()).isEqualTo("Printemps")
        assertThat(tdElements2[4].text()).isEqualTo("削除する")

        val tdElements3 = trElements[2].select("td")
        assertThat(tdElements3[0].text()).isEqualTo("3")
        assertThat(tdElements3[1].text()).isEqualTo("ラブアローシュート！")
        assertThat(tdElements3[2].text()).isEqualTo("園田海未")
        assertThat(tdElements3[3].text()).isEqualTo("lily white")
        assertThat(tdElements3[4].text()).isEqualTo("削除する")
    }

    @Test
    fun ShouldGetSearchTitleResult() {
        val url = "/book?title=だよ"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")

        assertThat(trElements).hasSize(1)
        val tdElements = trElements[0].select("td")
        assertThat(tdElements[0].text()).isEqualTo("1")
        assertThat(tdElements[1].text()).isEqualTo("ファイトだよっ！")
        assertThat(tdElements[2].text()).isEqualTo("高坂穂乃果")
        assertThat(tdElements[3].text()).isEqualTo("Printemps")
        assertThat(tdElements[4].text()).isEqualTo("削除する")
    }

    @Test
    fun ShouldGetAuthorSearchResult() {
        val url = "/book?author_id=1"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")

        assertThat(trElements).hasSize(1)
        val tdElements = trElements[0].select("td")
        assertThat(tdElements[0].text()).isEqualTo("1")
        assertThat(tdElements[1].text()).isEqualTo("ファイトだよっ！")
        assertThat(tdElements[2].text()).isEqualTo("高坂穂乃果")
        assertThat(tdElements[3].text()).isEqualTo("Printemps")
        assertThat(tdElements[4].text()).isEqualTo("削除する")
    }

    @Test
    fun ShouldGetPublisherSearchResult() {
        val url = "/book?publisher_id=2"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")

        assertThat(trElements).hasSize(1)
        val tdElements = trElements[0].select("td")
        assertThat(tdElements[0].text()).isEqualTo("3")
        assertThat(tdElements[1].text()).isEqualTo("ラブアローシュート！")
        assertThat(tdElements[2].text()).isEqualTo("園田海未")
        assertThat(tdElements[3].text()).isEqualTo("lily white")
        assertThat(tdElements[4].text()).isEqualTo("削除する")
    }

    @Test
    fun ShouldNotGetTitleSearchResult() {
        val url = "/book?title=サイトだよっ"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")
        assertThat(trElements).hasSize(0)

        val div = Jsoup.parse(response).body().select("div.alert-info")
        assertThat(div.text()).contains("みつかりませんでした")
    }

    @Test
    fun ShouldNotGetAuthorAndPublisherResult() {
        val url = "/book?author_id=1&publisher_id=2"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")
        assertThat(trElements).hasSize(0)

        val div = Jsoup.parse(response).body().select("div.alert-info")
        assertThat(div.text()).contains("みつかりませんでした")
    }

    @Test
    fun ShouldAddBook() {
        val title = "幸せ行きのSMILING!"
        val redirectUrl = "/book"
        val addRequest = post("/book/add")
                .param("title", title)
                .param("author_id", "1")
                .param("publisher_id", "1")
        val addResponse = mock.perform(addRequest).andReturn().response
        assertThat(addResponse.status).isEqualTo(302)
        assertThat(addResponse.redirectedUrl).isEqualTo(redirectUrl)

        val response = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")
        assertThat(trElements).hasSize(4)

        val tdElements = trElements[3].select("td")
        assertThat(tdElements[1].text()).isEqualTo(title)
        assertThat(tdElements[2].text()).isEqualTo("高坂穂乃果")
        assertThat(tdElements[3].text()).isEqualTo("Printemps")

        val data = book.findById(Integer.parseInt(tdElements[0].text()))
        if (data != null) {
            book.delete(data)
        }
    }

    @Test
    fun ShouldBeBookAddEnptyTitleError() {
        val request = post("/book/add").param("title", "")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("タイトルは")
    }

    @Test
    fun ShouldBeBookAddLongTitleError() {
        val request = post("/book/add").param("title", "ほ".repeat(256))
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("タイトルは")
    }

    @Test
    fun ShouldBeBookAddInvalidAuthorIdError() {
        val request = post("/book/add")
                .param("title", "ほ".repeat(255))
                .param("author_id", "0")
                .param("publisher_id", "1")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("著者を")
    }

    @Test
    fun ShouldBeBookAddInvalidPublisherIdError() {
        val request = post("/book/add")
                .param("title", "ほ".repeat(255))
                .param("author_id", "1")
                .param("publisher_id", "0")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("出版社を")
    }

    @Test
    fun ShouldEditBook() {
        val fromName = "幸せ行きのSMILING!"
        val toName = "私たちは未来の花"
        val redirectUrl = "/book"
        val addBookRequest = post("/book/add")
                .param("title", fromName)
                .param("author_id", "1")
                .param("publisher_id", "1")
        val addBookResponse = mock.perform(addBookRequest).andReturn().response
        assertThat(addBookResponse.status).isEqualTo(302)
        assertThat(addBookResponse.redirectedUrl).isEqualTo(redirectUrl)

        val getBookResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val fromTrElements = Jsoup.parse(getBookResponse).body().select("tbody tr")
        val fromTdElements = fromTrElements[3].select("td")
        assertThat(fromTrElements).hasSize(4)
        assertThat(fromTdElements[1].text()).isEqualTo(fromName)
        assertThat(fromTdElements[2].text()).isEqualTo("高坂穂乃果")
        assertThat(fromTdElements[3].text()).isEqualTo("Printemps")

        val book_id = Integer.parseInt(fromTdElements[0].text())
        val url = "/book/edit/%d".format(book_id)
        val getFormRequest = mock.perform(get(url)).andReturn().response.getContentAsString()
        val name = Jsoup.parse(getFormRequest).body().selectFirst("input[name=\"title\"]").attr("value")
        assertThat(name).isEqualTo(fromName)

        val editBookRequest = post(url)
                .param("title", toName)
                .param("author_id", "3")
                .param("publisher_id", "2")
        val editBookResponse = mock.perform(editBookRequest).andReturn().response
        assertThat(editBookResponse.status).isEqualTo(302)
        assertThat(editBookResponse.redirectedUrl).isEqualTo(redirectUrl)

        val toResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val toTrElements = Jsoup.parse(toResponse).body().select("tbody tr")
        val toTdElements = toTrElements[3].select("td")
        assertThat(toTrElements).hasSize(4)
        assertThat(toTdElements[1].text()).isEqualTo(toName)
        assertThat(toTdElements[2].text()).isEqualTo("園田海未")
        assertThat(toTdElements[3].text()).isEqualTo("lily white")

        val data = book.findById(book_id)
        if (data != null) {
            book.delete(data)
        }
    }

    @Test
    fun ShouldBeBookEditEnptyNameError() {
        val request = post("/book/edit/1").param("title", "")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("タイトルは")
    }

    @Test
    fun ShouldBeBookEditLongNameError() {
        val name = "ほ".repeat(256)
        val request = post("/book/edit/1").param("title", name)
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("タイトルは")
    }

    @Test
    fun ShouldBeBookEditInvalidAuthorIdError() {
        val request = post("/book/edit/1")
                .param("title", "ほ".repeat(255))
                .param("author_id", "0")
                .param("publisher_id", "1")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("著者を")
    }

    @Test
    fun ShouldBeBookEditInvalidPublisherIdError() {
        val request = post("/book/edit/1")
                .param("title", "ほ".repeat(255))
                .param("author_id", "1")
                .param("publisher_id", "0")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("出版社を")
    }

    @Test
    fun ShouldDeleteBook() {
        val name = "幸せ行きのSMILING!"
        val redirectUrl = "/book"
        val addBookRequest = post("/book/add")
                .param("title", name)
                .param("author_id", "1")
                .param("publisher_id", "1")
        val addBookResponse = mock.perform(addBookRequest).andReturn().response
        assertThat(addBookResponse.status).isEqualTo(302)
        assertThat(addBookResponse.redirectedUrl).isEqualTo(redirectUrl)

        val getBookResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val fromTrElements = Jsoup.parse(getBookResponse).body().select("tbody tr")
        val fromTdElements = fromTrElements[3].select("td")
        assertThat(fromTrElements).hasSize(4)

        val book_id = Integer.parseInt(fromTdElements[0].text())
        val url = "/book/remove/%d".format(book_id)
        val editBookResponse = mock.perform(get(url)).andReturn().response
        assertThat(editBookResponse.status).isEqualTo(302)
        assertThat(editBookResponse.redirectedUrl).isEqualTo(redirectUrl)

        val toResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val toTrElements = Jsoup.parse(toResponse).body().select("tbody tr")
        assertThat(toTrElements).hasSize(3)
    }
}
