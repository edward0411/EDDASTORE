// com/example/eddastore/data/repo/ProductRepository.kt
package com.example.eddastore.data.repo

import com.example.eddastore.data.local.dao.ProductDao
import com.example.eddastore.data.local.entity.toDomain
import com.example.eddastore.data.local.entity.toEntity
import com.example.eddastore.domain.model.Product
import kotlinx.coroutines.flow.map

class ProductRepository(private val dao: ProductDao) {

    fun observeAll() =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeActive() =
        dao.observeActive().map { list -> list.map { it.toDomain() } }

    suspend fun upsert(p: Product) =
        dao.upsert(p.toEntity())

    suspend fun setActive(id: Long, active: Boolean) =
        dao.setActive(id, active)

    suspend fun count() = dao.count()

    suspend fun get(id: Long): Product? = dao.getById(id)?.toDomain()
}
