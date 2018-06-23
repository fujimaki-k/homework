package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.service.SanitizeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class GreetingController {
    @GetMapping("/hello", "/hello/{name}")
    fun hello(model: Model, sanitize: SanitizeService, @PathVariable("name", required = false) name: String?): String {
        val message = "Hello, %s.".format(sanitize.normalize(name?: "world"))
        model.addAttribute("message", message)

        return "hello/index"
    }
}
