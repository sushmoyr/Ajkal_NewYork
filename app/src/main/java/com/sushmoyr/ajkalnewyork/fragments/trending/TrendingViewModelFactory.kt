package com.sushmoyr.ajkalnewyork.fragments.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource
import com.sushmoyr.ajkalnewyork.repository.Repository

class TrendingViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrendingViewModel(repository) as T
    }

}