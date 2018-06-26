package com.fujimakishouten.homework.repository

import com.fujimakishouten.homework.entity.AuthorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthorRepository: JpaRepository<AuthorEntity, Int> {
    fun findByNameContainingOrderByNameAsc(name: String): List<AuthorEntity>
}
