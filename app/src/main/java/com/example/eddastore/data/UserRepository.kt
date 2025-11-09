package com.example.eddastore.data

import android.content.Context
import com.example.eddastore.data.local.AppDb
import com.example.eddastore.data.local.entity.UserEntity

class UserRepository(context: Context) {
    private val dao = AppDb.get(context).userDao()

    suspend fun login(email: String, pass: String): UserEntity? {
        return dao.login(email.trim(), pass.trim())
    }

    suspend fun exists(email: String): Boolean {
        return dao.getByEmail(email.trim()) != null
    }

    suspend fun register(fullName: String, email: String, pass: String): Result<Long> {
        val user = UserEntity(fullName = fullName.trim(), email = email.trim(), password = pass.trim())
        return runCatching { dao.insert(user) }
    }
}
