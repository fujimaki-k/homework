package com.fujimakishouten.homework.service

import com.fujimakishouten.homework.entity.BookEntity
import com.fujimakishouten.homework.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookService {
    @Autowired
    lateinit var repository: BookRepository

    /**
     * 全ての本のデータを取得する
     *
     * @return List<BookEntity>
     */
    fun findAll(): List<BookEntity> {
        return repository.findAll()
    }

    /**
     * 本のタイトルを指定して該当するものを取得する
     *
     * @param String    本のタイトルまたはその一部
     */
    fun findByTitle(title: String): List<BookEntity> {
        return repository.findByTitleContainingOrderByTitleAsc(title)
    }

    /**
     * 本 ID を指定して出版社データを取得する
     *
     * @param Int 本 ID
     */
    fun findById(id: Int): BookEntity? {
        return repository.findById(id).orElse(null)
    }

    /**
     * 本データを保存する
     *
     * @param BookEntity
     * @return BookEntity
     */
    fun save(data: BookEntity): BookEntity {
        return repository.save(data)
    }

    /**
     * 本データを削除する
     *
     * @param BookEntity
     */
    fun delete(data: BookEntity) {
        return repository.delete(data)
    }
}
