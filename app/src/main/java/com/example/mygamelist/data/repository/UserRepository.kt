package com.example.mygamelist.data.repository

import com.example.mygamelist.data.local.UserDao
import com.example.mygamelist.data.local.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun login(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getUserByUsername(username)
    }

    suspend fun insertUser(user: UserEntity): Long {
        return userDao.insertUser(user)
    }

    suspend fun checkUsernameExists(username: String): Boolean {
        return userDao.checkUsernameExists(username) > 0
    }
}