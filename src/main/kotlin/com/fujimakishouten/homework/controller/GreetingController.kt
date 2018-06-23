package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.service.SanitizeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView

@Controller
class GreetingController {
    @GetMapping("/hello", "/hello/{name}")
    fun hello(mav: ModelAndView, sanitize: SanitizeService, @PathVariable("name", required = false) name: String?): ModelAndView {
        val message = "Hello, %s.".format(sanitize.normalize(name?: "world"))

        mav.addObject("message", message)
        mav.viewName = "hello/index"

        return mav
    }
}
