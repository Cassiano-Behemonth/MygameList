package com.example.mygamelist

data class Game(
    val name: String,
    val description: String,
    val achievementsCompleted: Int = 0,
    val totalAchievements: Int = 0,
    val completionPercentage: Int = 0,
    val status: GameStatus = GameStatus.IN_PROGRESS
)

enum class GameStatus {
    IN_PROGRESS,
    COMPLETED
}

data class User(
    val username: String,
    val password: String,
    val id: Int = 0
)