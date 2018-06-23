package com.fujimakishouten.homework.service

import java.text.Normalizer

class Sanitize {
    /**
     * 渡された文字列を正規化する
     *
     * @param string    正規化する文字列
     * @param [form]    正規化の方式、デフォルトは Normalizer.Form.NFKC
     * @return 正規化された文字列
     */
    fun normalize(string: String, form: Normalizer.Form=Normalizer.Form.NFKC): String {
        val lines = string.split(Regex("\r\n|\r|\n")).map(fun(line: String): String {
            return line.replace(Regex("\\p{C}"), "")
        }).toList()


        return Normalizer.normalize(lines.joinToString("\n"), form).trim()
    }
}
