package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.entity.PublisherEntity
import com.fujimakishouten.homework.service.PublisherService
import com.fujimakishouten.homework.service.SanitizeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class PublisherController {
    @Autowired
    lateinit var publisher: PublisherService;

    @GetMapping("/publisher", "/publisher/index")
    fun getIndex(model: ModelAndView): ModelAndView {
        val data = publisher.findAll()
        model.viewName = "publisher/index"
        model.addObject("publishers", data)

        return model
    }

    @GetMapping("/publisher/add")
    fun getAdd(model: ModelAndView, sanitize: SanitizeService): ModelAndView {
        model.viewName = "publisher/form"
        model.addObject("publisher", PublisherEntity())

        return model
    }

    @PostMapping("/publisher/add")
    fun postAdd(model: ModelAndView, @ModelAttribute("formData") data: PublisherEntity): ModelAndView {
        val parameters = publisher.sanitize(data)
        val errors = publisher.validate(parameters)
        if (errors.isEmpty()) {
            publisher.save(parameters)
            model.viewName = "redirect:/publisher"

            return model
        }

        model.viewName = "publisher/form"
        model.addObject("errors", errors)
        model.addObject("publisher", data)

        return model
    }

    @GetMapping("/publisher/edit/{publisher_id}")
    fun getEdit(model: ModelAndView, @PathVariable publisher_id: Int, @ModelAttribute("formData") data: PublisherEntity): ModelAndView {
        val parameters = publisher.findById(publisher_id)
        model.viewName = "publisher/form"
        model.addObject("publisher", parameters?: PublisherEntity())

        return model
    }

    @PostMapping("/publisher/edit/{publisher_id}")
    fun postEdit(model: ModelAndView, @PathVariable publisher_id: Int, @ModelAttribute("formData") data: PublisherEntity): ModelAndView {
        val parameters = publisher.findById(publisher_id)
        if (parameters == null) {
            model.viewName = "redirect:/publisher"

            return model
        }

        val sanitized = publisher.sanitize(data)
        parameters.name = sanitized.name

        val errors = publisher.validate(parameters)
        if (errors.isEmpty()) {
            publisher.save(parameters)
            model.viewName = "redirect:/publisher"

            return model
        }

        model.viewName = "publisher/form"
        model.addObject("errors", errors)
        model.addObject("publisher", data)

        return model
    }

    @GetMapping("/publisher/remove/{publisher_id}")
    fun getRemove(model: ModelAndView, @PathVariable publisher_id: Int): ModelAndView {
        val data = publisher.findById(publisher_id)
        if (data != null) {
            publisher.delete(data)
        }

        model.viewName = "redirect:/publisher"

        return model
    }
}
