package com.sushmoyr.ajkalnewyork.datasource.api

import android.content.Context
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import com.sushmoyr.ajkalnewyork.utils.Constants.MOCK_API_BASE_URL
import com.sushmoyr.ajkalnewyork.utils.Constants.MOCK_API_BASE_URL2
import com.sushmoyr.ajkalnewyork.utils.Constants.STRIPE_BACKEND_URL
import com.sushmoyr.ajkalnewyork.utils.MainApplication
import com.sushmoyr.ajkalnewyork.utils.hasNetwork
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {


    private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()
    private val context by lazy {
        MainApplication.applicationContext()
    }
    private val cache = Cache(context.cacheDir, CACHE_SIZE)

    private val okHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (hasNetwork(context)!!)
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
            chain.proceed(request)
        }
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val api: NewsApi by lazy {
        retrofit.create(NewsApi::class.java)
    }

    private val mockApiBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val mockApi: MockApi by lazy {
        mockApiBuilder.create(MockApi::class.java)
    }

    private val mockApiBuilder2 by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_API_BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val mockApi2 : MockApi by lazy {
        mockApiBuilder2.create(MockApi::class.java)
    }

    private val stripeBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(STRIPE_BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val stripeApi : StripeApi by lazy {
        stripeBuilder.create(StripeApi::class.java)
    }
}