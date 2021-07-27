package com.sushmoyr.ajkalnewyork.repository

import com.sushmoyr.ajkalnewyork.api.RetrofitInstance
import com.sushmoyr.ajkalnewyork.models.Category
import retrofit2.Response

class Repository {
    suspend fun getAllCategory(): Response<List<Category>>{
        return RetrofitInstance.api.getAllCategory()
    }
}