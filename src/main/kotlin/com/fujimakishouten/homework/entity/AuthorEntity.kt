package com.fujimakishouten.homework.entity

import javax.persistence.*

@Entity
@Table(name="authors")
data class AuthorEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="author_id")
    var author_id: Int? = null,

    @Column(name="name", length=255, nullable=false)
    var name: String = ""
)
