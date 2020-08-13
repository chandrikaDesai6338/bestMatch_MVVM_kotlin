package com.chan.marriagepraposals.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * Created by Chan on 11/08/20.
 */

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun UserDao() : UserDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context?): AppDatabase? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context!!, AppDatabase::class.java, "local_db_mp").build()
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}