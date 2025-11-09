package com.example.eddastore.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.eddastore.data.local.entity.UserEntity
import com.example.eddastore.data.AuthState
import com.example.eddastore.data.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(app)

    /**
     * Login:
     *  - Si existe y la pass coincide -> onOk()
     *  - Si existe y la pass NO coincide -> onError("Contraseña incorrecta")
     *  - Si NO existe -> onNeedRegister(email)
     */
    fun tryLogin(
        email: String,
        pass: String,
        onOk: () -> Unit,
        onNeedRegister: (String) -> Unit,
        onError: (String?) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val user: UserEntity? = repo.login(email, pass)
                when {
                    user != null -> {
                        AuthState.currentUser.value = user
                        onOk()
                    }
                    repo.exists(email) -> {
                        onError("Contraseña incorrecta")
                    }
                    else -> {
                        onNeedRegister(email)
                    }
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error inesperado")
            }
        }
    }

    /**
     * Registro:
     *  - Si el correo ya existe -> onError("El correo ya está registrado.")
     *  - Si registra ok -> auto-login y onOk()
     */
    fun register(
        fullName: String,
        email: String,
        pass: String,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (repo.exists(email)) {
                    onError("El correo ya está registrado.")
                    return@launch
                }
                repo.register(fullName, email, pass)
                    .onSuccess {
                        val u = repo.login(email, pass)
                        AuthState.currentUser.value = u
                        onOk()
                    }
                    .onFailure { onError("No fue posible registrar el usuario.") }
            } catch (e: Exception) {
                onError(e.message ?: "Error registrando el usuario.")
            }
        }
    }
}
