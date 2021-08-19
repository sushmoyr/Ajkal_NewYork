package com.sushmoyr.ajkalnewyork.fragments.auth.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.datasource.local.UserDatabase
import com.sushmoyr.ajkalnewyork.models.User
import com.sushmoyr.ajkalnewyork.models.utility.RegisterRequest
import com.sushmoyr.ajkalnewyork.models.utility.RegistrationResponse
import com.sushmoyr.ajkalnewyork.repository.LocalDataSource
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllData: LiveData<List<User>>
    private val repository: LocalDataSource

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = LocalDataSource(userDao)
        readAllData = repository.readAllData
    }

    private val api =  Repository().remoteDataSource

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    suspend fun getEmail(email: String): LiveData<List<User>> {
        return repository.getEmail("%$email")
    }

    suspend fun register(request: RegisterRequest): Response<RegistrationResponse> {
        return api.register(request)
    }
}