package com.fujimakishouten.homework.controller

import com.fujimakishouten.homework.entity.BookEntity
import com.fujimakishouten.homework.service.AuthorService
import com.fujimakishouten.homework.service.BookService
import com.fujimakishouten.homework.service.PublisherService
import com.fujimakishouten.homework.service.SanitizeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
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
    fun index(model: ModelAndView, @RequestParam title: String?, @RequestParam author_id: Int?, @RequestParam publisher_id: Int?): ModelAndView {
        val data: List<BookEntity>
        val query = (title != null || author_id != null || publisher_id != null)
        val authors = author.findAll()
        val publishers = publisher.findAll()
        if (query) {
            data = book.search(title, author_id, publisher_id)
        } else {
            data = book.findAll()
        }

        model.viewName = "book/index"
        model.addObject("books", data)
        model.addObject("authors", authors)
        model.addObject("publishers", publishers)
        model.addObject("title", title?: "")
        model.addObject("author_id", author_id?: 0)
        model.addObject("publisher_id", publisher_id?: 0)
        model.addObject("query", query)

        return model
    }

    @GetMapping("/book/edit", "/book/edit/{book_id}")
    fun form(model: ModelAndView, @PathVariable(required = false) book_id: Int?, @ModelAttribute("formData") data: BookEntity): ModelAndView {
        val authors = author.findAll()
        val publishers = publisher.findAll()
        var parameters = BookEntity()
        if (book_id != null) {
            parameters = book.findById(book_id)?: parameters
        }

        model.viewName = "book/form"
        model.addObject("book", parameters)
        model.addObject("authors", authors)
        model.addObject("publishers", publishers)

        return model
    }

    @PostMapping("/book/edit", "/book/edit/{book_id}")
    fun edit(model: ModelAndView, @PathVariable(required = false) book_id: Int?, @ModelAttribute("formData") data: BookEntity): ModelAndView {
        var parameters = BookEntity()
        val sanitized = book.sanitize(data)
        if (book_id != null) {
            parameters = book.findById(book_id)?: parameters
        }
        parameters.title = sanitized.title
        parameters.author_id = sanitized.author_id
        parameters.publisher_id = sanitized.publisher_id

        // サニタイズ後のデータをバリデーションしたいので @Validated は使わない
        val errors = book.validate(parameters)
        if (errors.isEmpty()) {
            book.save(parameters)
            model.viewName = "redirect:/book"

            return model
        }

        val authors = author.findAll()
        val publishers = publisher.findAll()
        model.viewName = "book/form"
        model.addObject("errors", errors)
        model.addObject("book", parameters)
        model.addObject("authors", authors)
        model.addObject("publishers", publishers)

        return model
    }

    @GetMapping("/book/remove/{book_id}")
    fun remove(model: ModelAndView, @PathVariable book_id: Int): ModelAndView {
        val data = book.findById(book_id)
        if (data != null) {
            book.delete(data)
        }

        model.viewName = "redirect:/book"

        return model
    }
}
