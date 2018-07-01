package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.entity.PublisherEntity
import com.fujimakishouten.homework.service.BookService
import com.fujimakishouten.homework.service.PublisherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class PublisherController {
    @Autowired
    lateinit var publisher: PublisherService;

    @Autowired
    lateinit var book: BookService;

    @GetMapping("/publisher", "/publisher/index")
    fun index(model: ModelAndView, @RequestParam name: String?): ModelAndView {
        val data: List<PublisherEntity>
        val query = (name != null)
        if (query) {
            data = publisher.findByName(name as String)
        } else {
            data = publisher.findAll()
        }

        model.viewName = "publisher/index"
        model.addObject("publishers", data)
        model.addObject("name", name?: "")
        model.addObject("query", query)

        return model
    }

    @GetMapping("/publisher/edit", "/publisher/edit/{publisher_id}")
    fun form(model: ModelAndView, @PathVariable(required = false) publisher_id: Int?, @ModelAttribute("formData") data: PublisherEntity): ModelAndView {
        var parameters = PublisherEntity()
        if (publisher_id != null) {
            parameters = publisher.findById(publisher_id)?: parameters
        }

        model.viewName = "publisher/form"
        model.addObject("publisher", parameters)

        return model
    }

    @PostMapping("/publisher/edit", "/publisher/edit/{publisher_id}")
    fun edit(model: ModelAndView, @PathVariable(required = false) publisher_id: Int?, @ModelAttribute("formData") data: PublisherEntity): ModelAndView {
        var parameters = PublisherEntity()
        val sanitized = publisher.sanitize(data)
        if (publisher_id != null) {
            parameters = publisher.findById(publisher_id)?: parameters
        }
        parameters.name = sanitized.name

        // サニタイズ後のデータをバリデーションしたいので @Validated は使わない
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
    fun remove(model: ModelAndView, @PathVariable publisher_id: Int): ModelAndView {
        val data = publisher.findById(publisher_id)
        if (data == null) {
            model.viewName = "redirect:/publisher"

            return model
        }

        val books = book.search(null, null, publisher_id)
        if (books.size > 0) {
            model.viewName = "publisher/remove"
            model.addObject("publisher", data)

            return model
        }
        publisher.delete(data)
        model.viewName = "redirect:/publisher"

        return model
    }
}
