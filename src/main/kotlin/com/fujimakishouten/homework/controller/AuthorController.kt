package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.entity.AuthorEntity
import com.fujimakishouten.homework.service.AuthorService
import com.fujimakishouten.homework.service.SanitizeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class AuthorController {
    @Autowired
    lateinit var author: AuthorService;

    @GetMapping("/author", "/author/index")
    fun getIndex(model: ModelAndView): ModelAndView {
        val data = author.findAll()
        model.viewName = "author/index"
        model.addObject("authors", data)

        return model
    }

    @GetMapping("/author/add")
    fun getAdd(model: ModelAndView, sanitize: SanitizeService): ModelAndView {
        model.viewName = "author/form"
        model.addObject("author", AuthorEntity())

        return model
    }

    @PostMapping("/author/add")
    fun postAdd(model: ModelAndView, @ModelAttribute("formData") data: AuthorEntity): ModelAndView {
        // サニタイズ後のデータをバリデーションしたいので @Validated は使わない
        val parameters = author.sanitize(data)
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

    @GetMapping("/author/edit/{author_id}")
    fun getEdit(model: ModelAndView, @PathVariable author_id: Int, @ModelAttribute("formData") data: AuthorEntity): ModelAndView {
        val parameters = author.findById(author_id)
        model.viewName = "author/form"
        model.addObject("author", parameters?: AuthorEntity())

        return model
    }

    @PostMapping("/author/edit/{author_id}")
    fun postEdit(model: ModelAndView, @PathVariable author_id: Int, @ModelAttribute("formData") data: AuthorEntity): ModelAndView {
        val parameters = author.findById(author_id)
        if (parameters == null) {
            model.viewName = "redirect:/author"

            return model
        }

        val sanitized = author.sanitize(data)
        parameters.name = sanitized.name

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
    fun getRemove(model: ModelAndView, @PathVariable author_id: Int): ModelAndView {
        val data = author.findById(author_id)
        if (data != null) {
            author.delete(data)
        }

        model.viewName = "redirect:/author"

        return model
    }
}
