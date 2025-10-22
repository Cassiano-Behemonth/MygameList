package com.example.mygamelist.data.local


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "achievementsCompleted")
    val achievementsCompleted: Int = 0,

    @ColumnInfo(name = "totalAchievements")
    val totalAchievements: Int = 0,

    @ColumnInfo(name = "completionPercentage")
    val completionPercentage: Int = 0,

    @ColumnInfo(name = "status")
    val status: String = GameStatus.IN_PROGRESS.name,

    @ColumnInfo(name = "userId")
    val userId: Int
)
fun GameEntity.toGame(): Game {
    return Game(
        name = this.name,
        description = this.description,
        achievementsCompleted = this.achievementsCompleted,
        totalAchievements = this.totalAchievements,
        completionPercentage = this.completionPercentage,
        status = GameStatus.valueOf(this.status)
    )
}

fun Game.toGameEntity(userId: Int, id: Int = 0): GameEntity {
    return GameEntity(
        id = id,
        name = this.name,
        description = this.description,
        achievementsCompleted = this.achievementsCompleted,
        totalAchievements = this.totalAchievements,
        completionPercentage = this.completionPercentage,
        status = this.status.name,
        userId = userId
    )
}

