package com.sushmoyr.ajkalnewyork.api

import com.sushmoyr.ajkalnewyork.utils.Constants.BASE_URL
import com.sushmoyr.ajkalnewyork.utils.Constants.MOCK_API_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: NewsApi by lazy {
        retrofit.create(NewsApi::class.java)
    }

    private val mockApiBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val mockApi: MockApi by lazy {
        mockApiBuilder.create(MockApi::class.java)
    }
}