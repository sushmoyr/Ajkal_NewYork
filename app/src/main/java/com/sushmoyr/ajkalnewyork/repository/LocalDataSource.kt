package com.sushmoyr.ajkalnewyork.repository

import androidx.lifecycle.LiveData
import com.sushmoyr.ajkalnewyork.datasource.local.UserDao
import com.sushmoyr.ajkalnewyork.models.InvalidUser

class LocalDataSource(private val userDao: UserDao) {

    val readAllData: LiveData<List<InvalidUser>> = userDao.readAllData()

    suspend fun addUser(user: InvalidUser){
        userDao.addUser(user)
    }

    fun getEmail(email: String): LiveData<List<InvalidUser>> {
        return userDao.getEmail(email)
    }

    fun getUser(email: String, password: String): LiveData<List<InvalidUser>> {
        return userDao.getUser(email, password)
    }

    fun getUserById(uuid: String): LiveData<List<InvalidUser>> {
        return userDao.getUserById(uuid)
    }

    suspend fun updateUser(user: InvalidUser) {
        userDao.updateUser(user)
    }

}