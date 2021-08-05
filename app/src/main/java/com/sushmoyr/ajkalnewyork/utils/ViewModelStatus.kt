package com.sushmoyr.ajkalnewyork.utils

sealed class ViewModelStatus{

    class Loading(
        val isLoading : Boolean = false
    ):ViewModelStatus()

    class Result(
        val isSuccess:Boolean,
        val msg: String ? = null
    ):ViewModelStatus()
}
