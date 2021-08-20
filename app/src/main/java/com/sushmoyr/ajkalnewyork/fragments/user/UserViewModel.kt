package com.sushmoyr.ajkalnewyork.fragments.user

import android.app.Application
import androidx.lifecycle.*
import com.sushmoyr.ajkalnewyork.datasource.local.UserDatabase
import com.sushmoyr.ajkalnewyork.models.InvalidUser
import com.sushmoyr.ajkalnewyork.repository.LocalDataSource
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocalDataSource

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = LocalDataSource(userDao)
    }

    fun getUser(uuid: String): LiveData<List<InvalidUser>> {
        return repository.getUserById("%$uuid")
    }

    fun updateUser(user: InvalidUser) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }
}