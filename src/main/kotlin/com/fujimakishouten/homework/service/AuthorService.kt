package com.fujimakishouten.homework.service

import com.fujimakishouten.homework.entity.AuthorEntity
import com.fujimakishouten.homework.repository.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthorService {
    @Autowired
    lateinit var repository: AuthorRepository

    @Autowired
    lateinit var sanitize: SanitizeService

    /**
     * 全ての著者データを取得する
     *
     * @return List<AuthorEntity>
     */
    fun findAll(): List<AuthorEntity> {
        return repository.findAll()
    }

    /**
     * 著者 ID を指定して著者データを取得する
     *
     * @param Int 著者 ID
     */
    fun findById(id: Int): AuthorEntity? {
        return repository.findById(id).orElse(null)
    }

    /**
     * 著者データを保存する
     *
     * @param AuthorEntity
     * @return AuthorEntity
     */
    fun save(data: AuthorEntity): AuthorEntity {
        return repository.save(data)
    }

    /**
     * 著者データを削除する
     *
     * @param AuthorEntity
     */
    fun delete(data: AuthorEntity) {
        return repository.delete(data)
    }

    /**
     * 著者データを正規化する
     *
     * @param AutherEntity
     * @return AuthorEntity
     */
    fun sanitize(data: AuthorEntity): AuthorEntity {
        data.name = sanitize.normalize(data.name)

        return data
    }

    /**
     * 著者データをチェックする
     *
     * @param AuthorEntity
     * @return MutableMap<String, RuntimeException>
     */
    fun validate(data: AuthorEntity): MutableMap<String, RuntimeException> {
        val errors = mutableMapOf<String, RuntimeException>()

        if (data.name.length < 1 || data.name.length > 255) {
            errors.put("name", RuntimeException("Invalid name"))
        }

        return errors
    }
}
