package com.fujimakishouten.homework.controller

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.containsString
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class HelloControllerTests {

    @Autowired
    lateinit var mock: MockMvc

    @Test
    fun ShouldGetGreetingMessage() {
        val source = mock.perform(get("/hello")).andReturn().response.getContentAsString()
        val document = Jsoup.parse(source)
        val elements = document.body().select("p")
        assertThat(elements.text()).isEqualTo("Hello, world.")
    }

}
