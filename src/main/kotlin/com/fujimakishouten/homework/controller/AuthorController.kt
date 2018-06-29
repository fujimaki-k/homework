package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.entity.AuthorEntity
import com.fujimakishouten.homework.service.AuthorService
import com.fujimakishouten.homework.service.SanitizeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class AuthorController {
    @Autowired
    lateinit var author: AuthorService;

    @GetMapping("/author", "/author/index")
    fun index(model: ModelAndView, @RequestParam name: String?): ModelAndView {
        val data: List<AuthorEntity>
        val query = (name != null)
        if (query) {
            data = author.findByName(name as String)
        } else {
            data = author.findAll()
        }

        model.viewName = "author/index"
        model.addObject("authors", data)
        model.addObject("name", name?: "")
        model.addObject("query", query)

        return model
    }

    @GetMapping("/author/edit", "/author/edit/{author_id}")
    fun form(model: ModelAndView, @PathVariable(required = false) author_id: Int?, @ModelAttribute("formData") data: AuthorEntity): ModelAndView {
        var parameters = AuthorEntity()
        if (author_id != null) {
            parameters = author.findById(author_id)?: parameters
        }

        model.viewName = "author/form"
        model.addObject("author", parameters)

        return model
    }

    @PostMapping("/author/edit", "/author/edit/{author_id}")
    fun edit(model: ModelAndView, @PathVariable(required = false) author_id: Int?, @ModelAttribute("formData") data: AuthorEntity): ModelAndView {
        var parameters = AuthorEntity()
        val sanitized = author.sanitize(data)
        if (author_id != null) {
            parameters = author.findById(author_id)?: parameters
        }
        parameters.name = sanitized.name

        // サニタイズ後のデータをバリデーションしたいので @Validated は使わない
        val errors = author.validate(parameters)
        if (errors.isEmpty()) {
            author.save(parameters)
            model.viewName = "redirect:/author"

            return model
        }

        model.viewName = "author/form"
        model.addObject("errors", errors)
        model.addObject("author", data)

        return model
    }

    @GetMapping("/author/remove/{author_id}")
    fun remove(model: ModelAndView, @PathVariable author_id: Int): ModelAndView {
        val data = author.findById(author_id)
        if (data != null) {
            author.delete(data)
        }

        model.viewName = "redirect:/author"

        return model
    }
}
