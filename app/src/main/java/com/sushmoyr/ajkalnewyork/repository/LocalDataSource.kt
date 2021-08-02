package com.sushmoyr.ajkalnewyork.repository

import androidx.lifecycle.LiveData
import com.sushmoyr.ajkalnewyork.datasource.local.UserDao
import com.sushmoyr.ajkalnewyork.models.User

class LocalDataSource(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    fun getEmail(email: String): LiveData<List<User>> {
        return userDao.getEmail(email)
    }

    fun getUser(email: String, password: String): LiveData<List<User>> {
        return userDao.getUser(email, password)
    }
}