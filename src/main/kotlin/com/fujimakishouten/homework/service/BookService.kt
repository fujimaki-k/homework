package com.fujimakishouten.homework.service

import com.fujimakishouten.homework.entity.BookEntity
import com.fujimakishouten.homework.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.transaction.Transactional
import java.util.ArrayList
import javax.persistence.criteria.Predicate


@Service
class BookService {
    @Autowired
    lateinit var repository: BookRepository

    @Autowired
    lateinit var authorService: AuthorService

    @Autowired
    lateinit var publisherService: PublisherService

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var sanitize: SanitizeService

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

    fun search(title: String?, author_id: Int?, publisher_id: Int?): List<BookEntity> {
        val builder = entityManager.getCriteriaBuilder()
        val query = builder.createQuery(BookEntity::class.java)
        val root = query.from(BookEntity::class.java)
        val predicates: ArrayList<Predicate> = ArrayList()
        if (title != null) {
            predicates.add(builder.like(root.get<String>("title"), "%%%s%%".format(title)))
        }
        if (author_id != null) {
            predicates.add(builder.equal(root.get<String>("author_id"), author_id))
        }
        if (publisher_id != null) {
            predicates.add(builder.equal(root.get<String>("publisher_id"), publisher_id))
        }

        query.select(root)
        if (predicates.isNotEmpty()) {
            query.where(builder.and(*Array(predicates.size, {i -> predicates[i]})))
        }

        val result = entityManager.createQuery(query).resultList

        return result
    }

    /**
     * 本データを保存する
     *
     * @param BookEntity
     * @return BookEntity
     */
    @Transactional
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

    /**
     * 著者データを正規化する
     *
     * @param AutherEntity
     * @return AuthorEntity
     */
    fun sanitize(data: BookEntity): BookEntity {
        data.title = sanitize.normalize(data.title)

        return data
    }

    /**
     * 著者データをチェックする
     *
     * @param AuthorEntity
     * @throws RuntimeException
     */
    fun validate(data: BookEntity) {
        if (data.title.length < 1 || data.title.length > 255) {
            throw RuntimeException("Invalid name")
        }

        val author = authorService.findById(data.author_id)
        if (author == null) {
            throw RuntimeException("Invalid author ID")
        }

        val publisher = publisherService.findById(data.publisher_id)
        if (publisher == null) {
            throw RuntimeException("Invalid publisher ID")
        }
    }
}
