package com.sushmoyr.ajkalnewyork.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sushmoyr.ajkalnewyork.models.InvalidUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: InvalidUser)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<InvalidUser>>

    @Query("SELECT * FROM user_table WHERE email LIKE :email")
    fun getEmail(email: String): LiveData<List<InvalidUser>>

    @Query("SELECT * FROM user_table WHERE (email LIKE :email AND password LIKE :password)")
    fun getUser(email: String, password: String): LiveData<List<InvalidUser>>

    @Query("SELECT * FROM user_table WHERE id LIKE :uuid")
    fun getUserById(uuid: String): LiveData<List<InvalidUser>>

    @Update
    suspend fun updateUser(user: InvalidUser)
}