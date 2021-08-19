package com.sushmoyr.ajkalnewyork.fragments.auth.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sushmoyr.ajkalnewyork.datasource.local.UserDatabase
import com.sushmoyr.ajkalnewyork.models.User
import com.sushmoyr.ajkalnewyork.models.utility.LoginRequest
import com.sushmoyr.ajkalnewyork.models.utility.LoginResponse
import com.sushmoyr.ajkalnewyork.repository.LocalDataSource
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource
import com.sushmoyr.ajkalnewyork.repository.Repository
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocalDataSource
    private val api = Repository().remoteDataSource

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = LocalDataSource(userDao)
    }

    fun getUser(email: String, password: String): LiveData<List<User>> {
        return repository.getUser("%$email", "%$password")
    }

    suspend fun loginWithApi(email: String, password: String): Response<LoginResponse> {
        return api.loginUser(LoginRequest(email, password))
    }

    suspend fun authUser() {
        api.authUser()
    }

    suspend fun logout() {
        api.logout()
    }
}