package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.service.AuthorService
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
class AuthorControllerTests {

    @Autowired
    lateinit var mock: MockMvc

    @Autowired
    lateinit var author: AuthorService

    @Test
    fun ShouldGetAuthorList() {
        val url = "/author"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")

        assertThat(trElements).hasSize(3)
        val tdElements1 = trElements[0].select("td")
        assertThat(tdElements1[0].text()).isEqualTo("1")
        assertThat(tdElements1[1].text()).isEqualTo("高坂穂乃果")
        assertThat(tdElements1[2].text()).isEqualTo("削除する")

        val tdElements2 = trElements[1].select("td")
        assertThat(tdElements2[0].text()).isEqualTo("2")
        assertThat(tdElements2[1].text()).isEqualTo("南ことり")
        assertThat(tdElements2[2].text()).isEqualTo("削除する")

        val tdElements3 = trElements[2].select("td")
        assertThat(tdElements3[0].text()).isEqualTo("3")
        assertThat(tdElements3[1].text()).isEqualTo("園田海未")
        assertThat(tdElements3[2].text()).isEqualTo("削除する")
    }

    @Test
    fun ShouldGetSearchResult() {
        val url = "/author?name=穂"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")

        assertThat(trElements).hasSize(1)
        val tdElements = trElements[0].select("td")
        assertThat(tdElements[0].text()).isEqualTo("1")
        assertThat(tdElements[1].text()).isEqualTo("高坂穂乃果")
        assertThat(tdElements[2].text()).isEqualTo("削除する")
    }

    @Test
    fun ShouldNotGetSearchResult() {
        val url = "/author?name=雪"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")
        assertThat(trElements).hasSize(0)

        val div = Jsoup.parse(response).body().select("div.alert-info")
        assertThat(div.text()).contains("みつかりませんでした")
    }

    @Test
    fun ShouldAddAuthor() {
        val name = "西木野真姫"
        val redirectUrl = "/author"
        val addRequest = post("/author/edit").param("name", name)
        val addResponse = mock.perform(addRequest).andReturn().response
        assertThat(addResponse.status).isEqualTo(302)
        assertThat(addResponse.redirectedUrl).isEqualTo(redirectUrl)

        val response = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")
        assertThat(trElements).hasSize(4)

        val tdElements = trElements[3].select("td")
        assertThat(tdElements[1].text()).isEqualTo(name)

        val data = author.findById(Integer.parseInt(tdElements[0].text()))
        if (data != null) {
            author.delete(data)
        }
    }

    @Test
    fun ShouldBeAuthorAddEnptyNameError() {
        val request = post("/author/edit").param("name", "")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("著者名は")
    }

    @Test
    fun ShouldBeAuthorAddLongNameError() {
        val request = post("/author/edit").param("name", "ほ".repeat(256))
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("著者名は")
    }

    @Test
    fun ShouldEditAuthor() {
        val fromName = "星空凛"
        val toName = "小泉花陽"
        val redirectUrl = "/author"
        val addAuthorRequest = post("/author/edit").param("name", fromName)
        val addAuthorResponse = mock.perform(addAuthorRequest).andReturn().response
        assertThat(addAuthorResponse.status).isEqualTo(302)
        assertThat(addAuthorResponse.redirectedUrl).isEqualTo(redirectUrl)

        val getAuthorResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val fromTrElements = Jsoup.parse(getAuthorResponse).body().select("tbody tr")
        val fromTdElements = fromTrElements[3].select("td")
        assertThat(fromTrElements).hasSize(4)
        assertThat(fromTdElements[1].text()).isEqualTo(fromName)

        val author_id = Integer.parseInt(fromTdElements[0].text())
        val url = "/author/edit/%d".format(author_id)
        val getFormRequest = mock.perform(get(url)).andReturn().response.getContentAsString()
        val name = Jsoup.parse(getFormRequest).body().selectFirst("input[name=\"name\"]").attr("value")
        assertThat(name).isEqualTo(fromName)

        val editAuthorRequest = post(url).param("name", toName)
        val editAuthorResponse = mock.perform(editAuthorRequest).andReturn().response
        assertThat(editAuthorResponse.status).isEqualTo(302)
        assertThat(editAuthorResponse.redirectedUrl).isEqualTo(redirectUrl)

        val toResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val toTrElements = Jsoup.parse(toResponse).body().select("tbody tr")
        val toTdElements = toTrElements[3].select("td")
        assertThat(toTrElements).hasSize(4)
        assertThat(toTdElements[1].text()).isEqualTo(toName)

        val data = author.findById(author_id)
        if (data != null) {
            author.delete(data)
        }
    }

    @Test
    fun ShouldBeAuthorEditEnptyNameError() {
        val request = post("/author/edit/1").param("name", "")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("著者名は")
    }

    @Test
    fun ShouldBeAuthorEditLongNameError() {
        val name = "ほ".repeat(256)
        val request = post("/author/edit/1").param("name", name)
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("著者名は")
    }

    @Test
    fun ShouldDeleteAuthor() {
        val name = "矢澤にこ"
        val redirectUrl = "/author"
        val addAuthorRequest = post("/author/edit").param("name", name)
        val addAuthorResponse = mock.perform(addAuthorRequest).andReturn().response
        assertThat(addAuthorResponse.status).isEqualTo(302)
        assertThat(addAuthorResponse.redirectedUrl).isEqualTo(redirectUrl)

        val getAuthorResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val fromTrElements = Jsoup.parse(getAuthorResponse).body().select("tbody tr")
        val fromTdElements = fromTrElements[3].select("td")
        assertThat(fromTrElements).hasSize(4)

        val author_id = Integer.parseInt(fromTdElements[0].text())
        val url = "/author/remove/%d".format(author_id)
        val editAuthorResponse = mock.perform(get(url)).andReturn().response
        assertThat(editAuthorResponse.status).isEqualTo(302)
        assertThat(editAuthorResponse.redirectedUrl).isEqualTo(redirectUrl)

        val toResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val toTrElements = Jsoup.parse(toResponse).body().select("tbody tr")
        assertThat(toTrElements).hasSize(3)
    }
}
