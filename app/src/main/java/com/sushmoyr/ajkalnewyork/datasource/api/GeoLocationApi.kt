package com.sushmoyr.ajkalnewyork.datasource.api

import com.sushmoyr.ajkalnewyork.models.utility.GeoLocationInfo
import retrofit2.Response
import retrofit2.http.GET

interface GeoLocationApi {


    @GET("json/")
    suspend fun getSessionGeoLocationInfo(): Response<GeoLocationInfo>
}