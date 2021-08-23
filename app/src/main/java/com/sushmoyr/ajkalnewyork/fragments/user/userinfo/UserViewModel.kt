package com.sushmoyr.ajkalnewyork.fragments.user.userinfo

import android.app.Application
import androidx.lifecycle.*
import com.sushmoyr.ajkalnewyork.datasource.local.UserDatabase
import com.sushmoyr.ajkalnewyork.models.InvalidUser
import com.sushmoyr.ajkalnewyork.models.UploadResponse
import com.sushmoyr.ajkalnewyork.models.utility.ProfileUpdateRequest
import com.sushmoyr.ajkalnewyork.models.utility.UpdatePasswordRequest
import com.sushmoyr.ajkalnewyork.repository.LocalDataSource
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val localDataSource: LocalDataSource
    private val repository = Repository().remoteDataSource


    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        localDataSource = LocalDataSource(userDao)
    }

    fun getUser(uuid: String): LiveData<List<InvalidUser>> {
        return localDataSource.getUserById("%$uuid")
    }

    suspend fun updateUserByApi(id: String ,requestBody: ProfileUpdateRequest)
    :Response<UploadResponse> {
        return repository.updateUser(id, requestBody)
    }

    val loader = MutableLiveData<Boolean>()
    fun setLoadingState(value: Boolean) {
        loader.value = value
    }

    suspend fun uploadProfileImage(id: Int, profileImage: MultipartBody.Part) : Response<UploadResponse> {
        return repository.uploadProfileImage(id, profileImage)
    }

    val updatePasswordLoader = MutableLiveData<Boolean>()
    fun setUpdatePasswordLoadingState(value: Boolean) {
        updatePasswordLoader.value = value
    }
    suspend fun updatePassword(id: String, request: UpdatePasswordRequest): Response<UploadResponse> {
        return repository.updatePassword(id, request)
    }

}