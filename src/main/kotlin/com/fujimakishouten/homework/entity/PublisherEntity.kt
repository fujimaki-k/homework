package com.fujimakishouten.homework.entity

import javax.persistence.*

@Entity
@Table(name="publishers")
data class PublisherEntity (
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="publisher_id")
    var publisher_id: Int? = null,

        @Column(name="name", length=255, nullable=false)
    var name: String = ""
)
