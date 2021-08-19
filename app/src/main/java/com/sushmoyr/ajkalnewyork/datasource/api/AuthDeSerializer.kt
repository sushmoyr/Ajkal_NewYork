package com.sushmoyr.ajkalnewyork.datasource.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.sushmoyr.ajkalnewyork.models.utility.LoginResponse
import java.lang.reflect.Type

class AuthDeSerializer: JsonDeserializer<LoginResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LoginResponse {
        checkNotNull(json)
        checkNotNull(typeOfT)
        checkNotNull(context)
        val jsonObject = json.asJsonObject
        Log.d("customSerial", jsonObject.toString())
        val type:String? = jsonObject.get("token").asString
        Log.d("customSerial", type?:"null")
        return when(type){
            null -> Gson().fromJson(jsonObject, LoginResponse.Error::class.java)
            else -> Gson().fromJson(jsonObject, LoginResponse.Success::class.java)
        }
    }
}