package com.example.mygamelist.data.repository

import com.example.mygamelist.data.local.GameDao
import com.example.mygamelist.data.local.GameEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDao: GameDao) {

    fun getAllGamesByUser(userId: Int): Flow<List<GameEntity>> {
        return gameDao.getAllGamesByUser(userId)
    }

    suspend fun insertGame(game: GameEntity): Long {
        return gameDao.insertGame(game)
    }

    suspend fun updateGame(game: GameEntity) {
        gameDao.updateGame(game)
    }

    suspend fun deleteGameById(gameId: Int) {
        gameDao.deleteGameById(gameId)
    }
}