package com.sushmoyr.ajkalnewyork.fragments.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.datasource.api.RetrofitInstance
import com.sushmoyr.ajkalnewyork.models.UserState
import kotlinx.coroutines.launch

class UserStateViewModel: ViewModel() {

    val userStateUpdater = MutableLiveData<UserState>()

    fun updateUserStateFromNetwork(id: String){
        viewModelScope.launch {
            val response = RetrofitInstance.api.updateUserProfileInfo(id)
            if(response.isSuccessful){
                userStateUpdater.postValue(UserState((response.body()!=null), response.body()))
            }
        }
    }
}