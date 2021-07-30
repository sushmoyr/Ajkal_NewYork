package com.sushmoyr.ajkalnewyork.activities.viewmodels

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawerViewModel: ViewModel() {
    var data: String = ""
    var livedata = MutableLiveData<String>()

    fun showData(){
        Log.d("viewmodel", data)
    }
    fun setValue(msg: String){
        livedata.postValue(msg)
    }
}