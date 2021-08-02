package com.sushmoyr.ajkalnewyork.fragments.auth.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sushmoyr.ajkalnewyork.datasource.local.UserDatabase
import com.sushmoyr.ajkalnewyork.models.User
import com.sushmoyr.ajkalnewyork.repository.LocalDataSource

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocalDataSource

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = LocalDataSource(userDao)
    }

    fun getUser(email: String, password: String): LiveData<List<User>> {
        return repository.getUser("%$email", "%$password")
    }
}