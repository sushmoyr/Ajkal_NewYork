package com.sushmoyr.ajkalnewyork.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sushmoyr.ajkalnewyork.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE email LIKE :email")
    fun getEmail(email: String): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE (email LIKE :email AND password LIKE :password)")
    fun getUser(email: String, password: String): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE id LIKE :uuid")
    fun getUserById(uuid: String): LiveData<List<User>>

    @Update
    suspend fun updateUser(user: User)
}