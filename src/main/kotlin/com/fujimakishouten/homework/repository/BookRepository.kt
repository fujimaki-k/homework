package com.fujimakishouten.homework.repository

import com.fujimakishouten.homework.entity.BookEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<BookEntity, Int> {
    fun findByTitleContainingOrderByTitleAsc(title: String): List<BookEntity>
}
