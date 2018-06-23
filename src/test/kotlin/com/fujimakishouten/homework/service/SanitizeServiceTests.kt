package com.fujimakishouten.homework.service

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
class SanitizeServiceTests {
    @Autowired
    lateinit var sanitize: SanitizeService

    @Test
    fun ShouldNormalizeText() {
        val string = "    \u202b１２３\r\nＡＢＣ\rｄｅｆ\nｱｲｳｴｵｶﾞ    "
        val result = "123\nABC\ndef\nアイウエオガ"

        assertThat(sanitize.normalize(string)).isEqualTo(result)
    }

}
