package com.fujimakishouten.homework.repository

import com.fujimakishouten.homework.entity.PublisherEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PublisherRepository: JpaRepository<PublisherEntity, Int> {
}
