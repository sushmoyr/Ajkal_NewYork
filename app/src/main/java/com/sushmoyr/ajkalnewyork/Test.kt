package com.sushmoyr.ajkalnewyork

import com.sushmoyr.ajkalnewyork.api.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object ViewModelObj{

    var callBack : ((value: String) -> Unit)?=null

    fun fetchNewsId(id: Int) = runBlocking{
        val datadeffered = async { RetrofitInstance.mockApi.getNewsById(id) }
        val data = datadeffered.await()
        if(data.isSuccessful){
            val body = data.body()!!
            callBack?.invoke(body[0].newsTitle)
        }
    }
}

fun main(){
    val viewModel = ViewModelObj
    viewModel.fetchNewsId(12)
    viewModel.callBack = { value ->
        println("value is $value")
    }
}