package com.fujimakishouten.homework.entity

import jdk.nashorn.internal.objects.annotations.Getter
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name="books")
data class BookEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    var book_id: Int? = null,

    @Column(name="title", length=255, nullable=false)
    @NotEmpty
    @Size(min=1, max=255)
    var title: String = "",

    @Column(name="author_id", nullable=false)
    var author_id: Int = 0,

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="author_id", insertable=false, updatable=false)
    var author: AuthorEntity? = null,

    @Column(name="publisher_id", nullable=false)
    var publisher_id: Int = 0,

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="publisher_id", insertable=false, updatable=false)
    var publisher: PublisherEntity? = null
)
