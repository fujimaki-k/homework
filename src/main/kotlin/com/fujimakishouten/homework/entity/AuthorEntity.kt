package com.fujimakishouten.homework.entity

import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name="authors")
data class AuthorEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="author_id")
    var author_id: Int? = null,

    @Column(name="name", length=255, nullable=false)
    @NotEmpty
    @Size(min=1, max=255)
    var name: String = ""
)
