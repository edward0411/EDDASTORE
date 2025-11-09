// com/example/eddastore/data/local/AppDb.kt
package com.example.eddastore.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.eddastore.data.local.dao.ProductDao
import com.example.eddastore.data.local.dao.UserDao
import com.example.eddastore.data.local.entity.ProductEntity
import com.example.eddastore.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserEntity::class, ProductEntity::class],
    version = 7,                         // ← sube la versión
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var INSTANCE: AppDb? = null

        fun get(context: Context): AppDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context): AppDb =
            Room.databaseBuilder(context, AppDb::class.java, "eddastore.db")
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {

                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            seedAdminIfMissing(context)
                            seedProductsIfEmpty(context)
                        }
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            // Garantiza la siembra aunque la BD ya existiera
                            seedAdminIfMissing(context)
                            seedProductsIfEmpty(context)
                        }
                    }

                    private suspend fun seedAdminIfMissing(context: Context) {
                        val udao = get(context).userDao()
                        val adminEmail = "edward0411@eddastore.com"
                        val exists = udao.getByEmail(adminEmail) != null
                        if (!exists) {
                            udao.insert(
                                UserEntity(
                                    fullName = "Edward Arevalo",
                                    email = adminEmail,
                                    password = "123456",
                                    role = "admin"
                                )
                            )
                        }
                    }

                    private suspend fun seedProductsIfEmpty(context: Context) {
                        val pdao = get(context).productDao()
                        if (pdao.count() == 0) {
                            pdao.insertAll(
                                ProductEntity(
                                    name = "Zapatillas Runner",
                                    description = "Ligera",
                                    price = 149_900.0,
                                    stock = 5,
                                    imagePath = "zapatillas_runner",
                                    isActive = true
                                ),
                                ProductEntity(
                                    name = "Tenis Urban",
                                    description = "Urbano",
                                    price = 129_900.0,
                                    stock = 8,
                                    imagePath = "tenis_urban",
                                    isActive = true
                                ),
                                ProductEntity(
                                    name = "Bota Trek",
                                    description = "Outdoor",
                                    price = 199_900.0,
                                    stock = 3,
                                    imagePath = "bota_trek",
                                    isActive = true
                                )
                            )
                        }
                    }

                })
                .build()
    }
}
