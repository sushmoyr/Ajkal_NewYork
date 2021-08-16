package com.sushmoyr.ajkalnewyork.utils

sealed class SearchState{
    data class Loading(val isLoading: Boolean): SearchState()
    data class Result(val hasResults: Boolean) : SearchState()
}
