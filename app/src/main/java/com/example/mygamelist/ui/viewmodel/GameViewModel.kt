package com.example.mygamelist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygamelist.data.local.GameEntity
import com.example.mygamelist.data.local.GameStatus
import com.example.mygamelist.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    private val _games = MutableStateFlow<List<GameEntity>>(emptyList())
    val games: StateFlow<List<GameEntity>> = _games.asStateFlow()

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Idle)
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun loadGames(userId: Int) {
        viewModelScope.launch {
            repository.getAllGamesByUser(userId).collect { gamesList ->
                _games.value = gamesList
            }
        }
    }

    fun saveGame(
        userId: Int,
        gameId: Int?,
        name: String,
        description: String,
        achievementsCompleted: Int,
        totalAchievements: Int,
        completionPercentage: Int,
        status: GameStatus
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = GameUiState.Loading

                val gameEntity = GameEntity(
                    id = gameId ?: 0,
                    name = name.trim(),
                    description = description.trim(),
                    achievementsCompleted = achievementsCompleted,
                    totalAchievements = totalAchievements,
                    completionPercentage = completionPercentage,
                    status = status.name,
                    userId = userId
                )

                if (gameId != null && gameId > 0) {
                    repository.updateGame(gameEntity)
                    _uiState.value = GameUiState.Success("Jogo atualizado com sucesso!")
                } else {
                    repository.insertGame(gameEntity)
                    _uiState.value = GameUiState.Success("Jogo cadastrado com sucesso!")
                }
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error("Erro ao salvar jogo: ${e.message}")
            }
        }
    }

    fun deleteGame(gameId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = GameUiState.Loading
                repository.deleteGameById(gameId)
                _uiState.value = GameUiState.Success("Jogo removido com sucesso!")
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error("Erro ao remover jogo: ${e.message}")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = GameUiState.Idle
    }
}

sealed class GameUiState {
    object Idle : GameUiState()
    object Loading : GameUiState()
    data class Success(val message: String) : GameUiState()
    data class Error(val message: String) : GameUiState()
}