package com.example.mygamelist.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String
)
fun UserEntity.toUser(): User {
    return User(
        username = this.username,
        password = this.password
    )
}

fun User.toUserEntity(id: Int = 0): UserEntity {
    return UserEntity(
        id = id,
        username = this.username,
        password = this.password
    )
}
