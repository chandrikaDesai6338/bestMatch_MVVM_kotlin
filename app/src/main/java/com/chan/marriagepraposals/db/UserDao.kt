package com.chan.marriagepraposals.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by Chan on 11/08/20.
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): Flowable<List<User>>

    @Query("SELECT * FROM user WHERE name LIKE :name")
    fun findUserByName(name : String) : Flowable<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: User) : Completable

    @Query("UPDATE user SET status = :updatedStatus WHERE uid = :userId")
    fun updateStatus(updatedStatus: String, userId: String) : Completable

    @Delete
    fun delete(user: User): Completable

}