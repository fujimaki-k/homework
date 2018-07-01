package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.service.SanitizeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView

@Controller
class IndexController {
    @GetMapping("/")
    fun index(mav: ModelAndView): ModelAndView {
        mav.viewName = "redirect:/book"

        return mav
    }
}
