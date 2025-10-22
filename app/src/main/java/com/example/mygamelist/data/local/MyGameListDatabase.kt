package com.example.mygamelist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [GameEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MyGameListDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: MyGameListDatabase? = null

        fun getDatabase(context: Context): MyGameListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyGameListDatabase::class.java,
                    "mygamelist_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}