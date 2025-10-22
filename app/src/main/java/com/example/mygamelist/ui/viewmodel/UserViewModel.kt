package com.example.mygamelist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygamelist.data.local.User
import com.example.mygamelist.data.local.UserEntity
import com.example.mygamelist.data.local.toUser
import com.example.mygamelist.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _currentUserId = MutableStateFlow(0)
    val currentUserId: StateFlow<Int> = _currentUserId.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                if (username.isBlank() || password.isBlank()) {
                    _authState.value = AuthState.Error("Preencha todos os campos!")
                    return@launch
                }

                val user = repository.login(username, password)

                if (user != null) {
                    _currentUser.value = user.toUser()
                    _currentUserId.value = user.id
                    _authState.value = AuthState.Success("Login realizado com sucesso!")
                } else {
                    _authState.value = AuthState.Error("Usuário ou senha inválidos")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro ao fazer login: ${e.message}")
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                if (username.isBlank() || password.isBlank()) {
                    _authState.value = AuthState.Error("Preencha todos os campos!")
                    return@launch
                }

                val exists = repository.checkUsernameExists(username)

                if (exists) {
                    _authState.value = AuthState.Error("Usuário já existe!")
                } else {
                    val newUser = UserEntity(username = username, password = password)
                    repository.insertUser(newUser)
                    _authState.value = AuthState.RegisterSuccess("Usuário cadastrado com sucesso!")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro ao cadastrar: ${e.message}")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentUserId.value = 0
        _authState.value = AuthState.LogoutSuccess
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class RegisterSuccess(val message: String) : AuthState()
    object LogoutSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}