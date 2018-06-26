package com.fujimakishouten.homework.controller

import org.hamcrest.Matchers.containsString
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

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class HelloControllerTests {

    @Autowired
    lateinit var mock: MockMvc

    @Test
    fun ShouldGetGreetingMessage() {
        mock.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, world.")))
        mock.perform(get("/hello/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, test.")))
    }

}
