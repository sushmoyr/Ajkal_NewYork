package com.sushmoyr.ajkalnewyork

sealed class NetworkResponse<T>(
    val response: T?=null,
    val exception: Exception? = null,
    val message: String? = null
){
    class Success<T>(data: T): NetworkResponse<T>(data)
    class Error<T>(exception: Exception?=null, data: T? = null, message: String?=null):
    NetworkResponse<T>
        (data, exception, message)
    class Loading<T>: NetworkResponse<T>()
}
