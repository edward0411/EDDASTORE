// com/example/eddastore/data/local/dao/ProductDao.kt
package com.example.eddastore.data.local.dao

import androidx.room.*
import com.example.eddastore.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM product ORDER BY name")
    fun observeAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product WHERE isActive = 1 ORDER BY name")
    fun observeActive(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ProductEntity): Long

    @Update
    suspend fun update(entity: ProductEntity)

    @Query("UPDATE product SET isActive = :active WHERE id = :id")
    suspend fun setActive(id: Long, active: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: ProductEntity)

    @Query("SELECT COUNT(*) FROM product")
    suspend fun count(): Int

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ProductEntity?

    @Query("SELECT stock FROM product WHERE id=:id") suspend fun getStock(id: Long): Int?
    @Query("UPDATE product SET stock = stock - :qty WHERE id=:id AND stock >= :qty")
    suspend fun decrementStock(id: Long, qty: Int)
}
