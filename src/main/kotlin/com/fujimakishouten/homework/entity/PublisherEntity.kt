package com.fujimakishouten.homework.entity

import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name="publishers")
data class PublisherEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="publisher_id")
    var publisher_id: Int? = null,

    @Column(name="name", length=255, nullable=false)
    @NotEmpty
    @Size(min=1, max=255)
    var name: String = ""
)
