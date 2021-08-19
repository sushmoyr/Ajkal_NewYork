package com.sushmoyr.ajkalnewyork.utils

sealed class ResourceState{

    class Loading(
        val isLoading : Boolean = false
    ):ResourceState()

    class Result(
        val isSuccess:Boolean,
        val msg: String ? = null
    ):ResourceState()
}
