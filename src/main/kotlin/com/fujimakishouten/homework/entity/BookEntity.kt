package com.fujimakishouten.homework.entity

import javax.persistence.*

@Entity
@Table(name="books")
data class BookEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    var book_id: Int? = null,

    @Column(name="title", length=255, nullable=false)
    var title: String = "",

    @Column(name="author_id", nullable=false)
    var author_id: Int = 0,

    @Column(name="publisher_id", nullable=false)
    var publisher_id: Int = 0
)
