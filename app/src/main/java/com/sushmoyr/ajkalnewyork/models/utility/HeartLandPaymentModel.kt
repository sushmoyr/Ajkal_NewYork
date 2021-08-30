package com.sushmoyr.ajkalnewyork.models.utility

import com.google.gson.annotations.SerializedName

data class HeartLandPaymentModel(
    val adId: String,
    val user_id: String,
    val user_email: String,
    val user_name: String,
    val user_mobile: String?,
    val cardExpMonth: String,
    val cardExpYear: String,
    val cardNumber: String,
    val cardCvv: String,
    @SerializedName("country_code")
    var countryCode: String = "",
    @SerializedName("country_name")
    var countryName: String = "",
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("postal")
    var postal: String? = null,
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("IPv4")
    var iPv4: String = "",
    @SerializedName("state")
    var state: String? = null
){
    fun setSessionInfo(location: GeoLocationInfo){
        countryCode = location.countryCode
        countryName = location.countryName
        city = location.city
        postal = location.postal
        latitude = location.latitude
        longitude = location.longitude
        iPv4 = location.iPv4
        state = location.state
    }
}
