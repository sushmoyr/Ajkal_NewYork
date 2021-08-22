package com.sushmoyr.ajkalnewyork.datasource.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sushmoyr.ajkalnewyork.models.utility.LoginResponse
import com.sushmoyr.ajkalnewyork.utils.Constants.AJKAL_BASE_URL
import com.sushmoyr.ajkalnewyork.utils.Constants.GEOLOCATION_URL
import com.sushmoyr.ajkalnewyork.utils.Constants.STRIPE_BACKEND_URL
import com.sushmoyr.ajkalnewyork.utils.MainApplication
import com.sushmoyr.ajkalnewyork.utils.hasNetwork
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {


    private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()
    private val context by lazy {
        MainApplication.applicationContext()
    }
    private val cache = Cache(context.cacheDir, CACHE_SIZE)

    /*private val okHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (hasNetwork(context)!!)
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
            chain.proceed(request)
        }
        .build()*/

   /* private val apiClient = OkHttpClient.Builder().apply {
        cache(cache)
        interceptors().add(interceptor)
        interceptors().add { chain ->
            var request = chain.request()
            request = if (hasNetwork(context))
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + (60 * 60 * 24 * 7)
                ).build()
            chain.proceed(request)
        }
    }.build()*/

    /*private val retrofit by lazy {
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
    }*/

    private val gson: Gson = GsonBuilder().setLenient().create()


    private val interceptor = HttpLoggingInterceptor().apply {
        level= HttpLoggingInterceptor.Level.BODY
    }
    private val httpClient = OkHttpClient.Builder().apply {
        interceptors().add(interceptor)
    }.build()

    private val apiBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(AJKAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
    }

    val api: AjkalApi by lazy {
        apiBuilder.create(AjkalApi::class.java)
    }

    private val geoApiBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(GEOLOCATION_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    val geoApi: GeoLocationApi by lazy {
        geoApiBuilder.create(GeoLocationApi::class.java)
    }

    private val customDeserializer: Gson = GsonBuilder().registerTypeAdapter(LoginResponse::class.java,
        AuthDeSerializer()).create()

    // Add other Interceptors
    val client = OkHttpClient.Builder().apply {
        addNetworkInterceptor(Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Accept","*/*")
                .method(original.method, original.body)
                .build()
            val response = chain.proceed(request)
            Log.d("from instance", "Code: ${response.code}")
            Log.d("from instance", "data: ${response.toString()}")
            response
        })
    }
    /*httpClient.addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
                .header("Authorization", token_type + " " + access_token)
                .method(original.method(), original.body())
                .build();

            Response response =  chain.proceed(request);
            Log.d("MyApp", "Code : "+response.code());
            if (response.code() == 401){
                // Magic is here ( Handle the error as your way )
                return response;
            }
            return response;
        }
    });*/

    private val authApiBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(AJKAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    val authApi: AuthApi by lazy {
        authApiBuilder.create(AuthApi::class.java)
    }



    private val stripeBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(STRIPE_BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val stripeApi: StripeApi by lazy {
        stripeBuilder.create(StripeApi::class.java)
    }
}


