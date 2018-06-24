package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.entity.BookEntity
import com.fujimakishouten.homework.service.AuthorService
import com.fujimakishouten.homework.service.BookService
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
class BookController {
    @Autowired
    lateinit var book: BookService

    @Autowired
    lateinit var author: AuthorService

    @Autowired
    lateinit var publisher: PublisherService

    @GetMapping("/book", "/book/index")
    fun getIndex(model: ModelAndView): ModelAndView {
        val data = book.findAll()
        model.viewName = "book/index"
        model.addObject("books", data)

        return model
    }

    @GetMapping("/book/add")
    fun getAdd(model: ModelAndView, sanitize: SanitizeService): ModelAndView {
        val authors = author.findAll()
        val publishers = publisher.findAll()

        model.viewName = "book/form"
        model.addObject("authors", authors)
        model.addObject("publishers", publishers)

        return model
    }

    @PostMapping("/book/add")
    fun postAdd(model: ModelAndView, @ModelAttribute("formData") data: BookEntity): ModelAndView {
        val parameters = book.sanitize(data)
        try {
            // サニタイズ後の値をバリデーションしたかったので @Validated ではなく独自で実装
            book.validate(parameters)
            book.save(parameters)

            model.viewName = "redirect:/book"
        } catch (e: RuntimeException) {
            val authors = author.findAll()
            val publishers = publisher.findAll()

            model.viewName = "book/form"
            model.addObject("authors", authors)
            model.addObject("publishers", publishers)
        }

        return model
    }

    @GetMapping("/book/edit/{book_id}")
    fun getEdit(model: ModelAndView, @PathVariable book_id: Int, @ModelAttribute("formData") data: BookEntity): ModelAndView {
        val authors = author.findAll()
        val publishers = publisher.findAll()
        val parameters = book.findById(book_id)

        model.viewName = "book/form"
        model.addObject("authors", authors)
        model.addObject("publishers", publishers)
        if (parameters != null) {
            model.addObject("book", parameters)
        }

        return model
    }

    @PostMapping("/book/edit/{book_id}")
    fun postEdit(model: ModelAndView, @PathVariable book_id: Int, @ModelAttribute("formData") data: BookEntity): ModelAndView {
        val parameters = book.findById(book_id)
        val form = book.sanitize(data)
        if (parameters == null) {
            model.viewName = "redirect:/book"

            return model
        }
        parameters.title = form.title
        parameters.author_id = form.author_id
        parameters.publisher_id = form.publisher_id

        try {
            // サニタイズ後の値をバリデーションしたかったので @Validated ではなく独自で実装
            book.validate(parameters)

            book.save(parameters)

            model.viewName = "redirect:/book"
        } catch (e: RuntimeException) {
            model.viewName = "book/form"
        }

        return model
    }

    @GetMapping("/book/remove/{book_id}")
    fun getRemove(model: ModelAndView, @PathVariable book_id: Int): ModelAndView {
        val data = book.findById(book_id)
        if (data != null) {
            book.delete(data)
        }

        model.viewName = "redirect:/book"

        return model
    }
}
