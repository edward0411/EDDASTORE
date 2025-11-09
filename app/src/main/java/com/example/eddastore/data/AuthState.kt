package com.example.eddastore.data

import androidx.compose.runtime.mutableStateOf
import com.example.eddastore.data.local.entity.UserEntity

object AuthState {
    val currentUser = mutableStateOf<UserEntity?>(null)
    fun isAdmin() = currentUser.value?.role == "admin"
    fun logout() { currentUser.value = null }
    fun isLogged() = currentUser.value != null
    fun displayName() = currentUser.value?.fullName ?: "Invitado"

}
