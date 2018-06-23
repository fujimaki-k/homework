package com.fujimakishouten.homework.service

import com.fujimakishouten.homework.entity.PublisherEntity
import com.fujimakishouten.homework.repository.PublisherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PublisherService {
    @Autowired
    lateinit var repository: PublisherRepository


    /**
     * 全ての出版社データを取得する
     *
     * @return List<PublisherEntity>
     */
    fun findAll(): List<PublisherEntity> {
        return repository.findAll()
    }

    /**
     * 出版社 ID を指定して出版社データを取得する
     *
     * @param Int 出版社 ID
     */
    fun findById(id: Int): PublisherEntity? {
        return repository.findById(id).orElse(null)
    }

    /**
     * 出版社データを保存する
     *
     * @param PublisherEntity
     * @return PublisherEntity
     */
    fun save(data: PublisherEntity): PublisherEntity {
        return repository.save(data)
    }

    /**
     * 出版社データを削除する
     *
     * @param PublisherEntity
     */
    fun delete(data: PublisherEntity) {
        return repository.delete(data)
    }
}
