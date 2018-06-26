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
    fun getIndex(model: ModelAndView, @RequestParam title: String?, @RequestParam author_id: Int?, @RequestParam publisher_id: Int?): ModelAndView {
        val data: List<BookEntity>
        val query: Boolean
        val authors = author.findAll()
        val publishers = publisher.findAll()
        if (title == null && author_id == null && publisher_id == null) {
            query = false
            data = book.findAll()
        } else {
            query = true
            data = book.search(title, author_id, publisher_id)
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

    @GetMapping("/book/add")
    fun getAdd(model: ModelAndView, sanitize: SanitizeService): ModelAndView {
        val authors = author.findAll()
        val publishers = publisher.findAll()

        model.viewName = "book/form"
        model.addObject("book", BookEntity())
        model.addObject("authors", authors)
        model.addObject("publishers", publishers)

        return model
    }

    @PostMapping("/book/add")
    fun postAdd(model: ModelAndView, @ModelAttribute("formData") data: BookEntity): ModelAndView {
        // サニタイズ後のデータをバリデーションしたいので @Validated は使わない
        val parameters = book.sanitize(data)
        val errors = book.validate(parameters)
        if (errors.isEmpty()) {
            book.save(parameters)
            model.viewName = "redirect:/book"

            return model
        }

        model.viewName = "book/form"
        model.addObject("errors", errors)
        model.addObject("book", data)

        return model
    }

    @GetMapping("/book/edit/{book_id}")
    fun getEdit(model: ModelAndView, @PathVariable book_id: Int, @ModelAttribute("formData") data: BookEntity): ModelAndView {
        val authors = author.findAll()
        val publishers = publisher.findAll()
        val parameters = book.findById(book_id)

        model.viewName = "book/form"
        model.addObject("book", parameters?: BookEntity())
        model.addObject("authors", authors)
        model.addObject("publishers", publishers)

        return model
    }

    @PostMapping("/book/edit/{book_id}")
    fun postEdit(model: ModelAndView, @PathVariable book_id: Int, @ModelAttribute("formData") data: BookEntity): ModelAndView {
        val parameters = book.findById(book_id)
        if (parameters == null) {
            model.viewName = "redirect:/book"

            return model
        }

        val sanitized = book.sanitize(data)
        parameters.title = sanitized.title
        parameters.author_id = sanitized.author_id
        parameters.publisher_id = sanitized.publisher_id

        val errors = book.validate(parameters)
        if (errors.isEmpty()) {
            book.save(parameters)
            model.viewName = "redirect:/book"

            return model
        }

        model.viewName = "book/form"
        model.addObject("errors", errors)
        model.addObject("book", data)

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
