package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.service.PublisherService
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
class PublisherControllerTests {

    @Autowired
    lateinit var mock: MockMvc

    @Autowired
    lateinit var publisher: PublisherService

    @Test
    fun ShouldGetPublisherList() {
        val url = "/publisher"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")

        assertThat(trElements).hasSize(2)
        val tdElements1 = trElements[0].select("td")
        assertThat(tdElements1[0].text()).isEqualTo("1")
        assertThat(tdElements1[1].text()).isEqualTo("Printemps")
        assertThat(tdElements1[2].text()).isEqualTo("削除する")

        val tdElements2 = trElements[1].select("td")
        assertThat(tdElements2[0].text()).isEqualTo("2")
        assertThat(tdElements2[1].text()).isEqualTo("lily white")
        assertThat(tdElements2[2].text()).isEqualTo("削除する")
    }

    @Test
    fun ShouldGetSearchResult() {
        val url = "/publisher?name=p"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")

        assertThat(trElements).hasSize(1)
        val tdElements = trElements[0].select("td")
        assertThat(tdElements[0].text()).isEqualTo("1")
        assertThat(tdElements[1].text()).isEqualTo("Printemps")
        assertThat(tdElements[2].text()).isEqualTo("削除する")
    }

    @Test
    fun ShouldNotGetSearchResult() {
        val url = "/publisher?name=b"
        val response = mock.perform(get(url)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")
        assertThat(trElements).hasSize(0)

        val div = Jsoup.parse(response).body().select("div.alert-info")
        assertThat(div.text()).contains("みつかりませんでした")
    }

    @Test
    fun ShouldAddPublisher() {
        val name = "BiBi"
        val redirectUrl = "/publisher"
        val addRequest = post("/publisher/edit").param("name", name)
        val addResponse = mock.perform(addRequest).andReturn().response
        assertThat(addResponse.status).isEqualTo(302)
        assertThat(addResponse.redirectedUrl).isEqualTo(redirectUrl)

        val response = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val trElements = Jsoup.parse(response).body().select("tbody tr")
        assertThat(trElements).hasSize(3)

        val tdElements = trElements[2].select("td")
        assertThat(tdElements[1].text()).isEqualTo(name)

        val data = publisher.findById(Integer.parseInt(tdElements[0].text()))
        if (data != null) {
            publisher.delete(data)
        }
    }

    @Test
    fun ShouldBePublisherAddEnptyNameError() {
        val request = post("/publisher/edit").param("name", "")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("出版社名は")
    }

    @Test
    fun ShouldBePublisherAddLongNameError() {
        val request = post("/publisher/edit").param("name", "ほ".repeat(256))
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("出版社名は")
    }

    @Test
    fun ShouldEditPublisher() {
        val fromName = "BiBi"
        val toName = "μ's"
        val redirectUrl = "/publisher"
        val addPublisherRequest = post("/publisher/edit").param("name", fromName)
        val addPublisherResponse = mock.perform(addPublisherRequest).andReturn().response
        assertThat(addPublisherResponse.status).isEqualTo(302)
        assertThat(addPublisherResponse.redirectedUrl).isEqualTo(redirectUrl)

        val getPublisherResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val fromTrElements = Jsoup.parse(getPublisherResponse).body().select("tbody tr")
        val fromTdElements = fromTrElements[2].select("td")
        assertThat(fromTrElements).hasSize(3)
        assertThat(fromTdElements[1].text()).isEqualTo(fromName)

        val publisher_id = Integer.parseInt(fromTdElements[0].text())
        val url = "/publisher/edit/%d".format(publisher_id)
        val getFormRequest = mock.perform(get(url)).andReturn().response.getContentAsString()
        val name = Jsoup.parse(getFormRequest).body().selectFirst("input[name=\"name\"]").attr("value")
        assertThat(name).isEqualTo(fromName)

        val editPublisherRequest = post(url).param("name", toName)
        val editPublisherResponse = mock.perform(editPublisherRequest).andReturn().response
        assertThat(editPublisherResponse.status).isEqualTo(302)
        assertThat(editPublisherResponse.redirectedUrl).isEqualTo(redirectUrl)

        val toResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val toTrElements = Jsoup.parse(toResponse).body().select("tbody tr")
        val toTdElements = toTrElements[2].select("td")
        assertThat(toTrElements).hasSize(3)
        assertThat(toTdElements[1].text()).isEqualTo(toName)

        val data = publisher.findById(publisher_id)
        if (data != null) {
            publisher.delete(data)
        }
    }

    @Test
    fun ShouldBePublisherEditEnptyNameError() {
        val request = post("/publisher/edit/1").param("name", "")
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("出版社名は")
    }

    @Test
    fun ShouldBePublisherEditLongNameError() {
        val name = "ほ".repeat(256)
        val request = post("/publisher/edit/1").param("name", name)
        val response = mock.perform(request).andReturn().response.getContentAsString()

        val alert = Jsoup.parse(response).body().selectFirst("div.alert-danger")
        val nameError = Jsoup.parse(response).body().selectFirst("p.text-danger")
        assertThat(alert.text()).contains("エラーが発生しました")
        assertThat(nameError.text()).contains("出版社名は")
    }

    @Test
    fun ShouldDeletePublisher() {
        val name = "BiBi"
        val redirectUrl = "/publisher"
        val addPublisherRequest = post("/publisher/edit").param("name", name)
        val addPublisherResponse = mock.perform(addPublisherRequest).andReturn().response
        assertThat(addPublisherResponse.status).isEqualTo(302)
        assertThat(addPublisherResponse.redirectedUrl).isEqualTo(redirectUrl)

        val getPublisherResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val fromTrElements = Jsoup.parse(getPublisherResponse).body().select("tbody tr")
        val fromTdElements = fromTrElements[2].select("td")
        assertThat(fromTrElements).hasSize(3)

        val publisher_id = Integer.parseInt(fromTdElements[0].text())
        val url = "/publisher/remove/%d".format(publisher_id)
        val editPublisherResponse = mock.perform(get(url)).andReturn().response
        assertThat(editPublisherResponse.status).isEqualTo(302)
        assertThat(editPublisherResponse.redirectedUrl).isEqualTo(redirectUrl)

        val toResponse = mock.perform(get(redirectUrl)).andReturn().response.getContentAsString()
        val toTrElements = Jsoup.parse(toResponse).body().select("tbody tr")
        assertThat(toTrElements).hasSize(2)
    }
}
