package com.fujimakishouten.homework.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class HomeworkApplicationTests {

    @Test
    fun ShouldNormalizeText() {
        val sanitize = Sanitize()
        val string = "    \u202b１２３\r\nＡＢＣ\rｄｅｆ\nｱｲｳｴｵｶﾞ    "
        val result = "123\nABC\ndef\nアイウエオガ"

        assertThat(sanitize.normalize(string)).isEqualTo(result)
    }

}
