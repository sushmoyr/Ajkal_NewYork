package com.sushmoyr.ajkalnewyork

sealed class NetworkResponse<T>(
    val response: T?=null,
    val exception: Exception? = null
){
    class Success<T>(data: T): NetworkResponse<T>(data)
    class Error<T>(exception: Exception?, data: T? = null): NetworkResponse<T>(data, exception)
}
