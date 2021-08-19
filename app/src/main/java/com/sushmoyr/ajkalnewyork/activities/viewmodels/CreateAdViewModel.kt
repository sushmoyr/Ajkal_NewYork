package com.sushmoyr.ajkalnewyork.activities.viewmodels

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stripe.android.Stripe
import com.stripe.android.getPaymentIntentResult
import com.stripe.android.model.StripeIntent
import com.sushmoyr.ajkalnewyork.models.core.Advertisement
import com.sushmoyr.ajkalnewyork.models.core.AdvertisementSize
import com.sushmoyr.ajkalnewyork.models.stripe.AdvertisementPayment
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentResponse
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class CreateAdViewModel: ViewModel() {

    val repository = Repository()
    val totalCost = MutableLiveData<Double>().also {
        it.value = 0.0
    }

    fun seTotalCost(cost: Double = 0.0, limit: Int = 1){
        totalCost.value = cost * limit
    }

    init {
        getAdSizes()
    }

    var didSomething: ((title: String,
                        message: String,
                        code: StripeIntent.Status) -> Unit) ? = null

    /*
    Stripe payment handle section starts here
     */

    fun handleStripe(stripe: Stripe, requestCode: Int, data: Intent?) {
        viewModelScope.launch {
            runCatching {
                stripe.getPaymentIntentResult(requestCode, data!!)
            }.fold(
                onSuccess = { result ->
                    val paymentIntent = result.intent
                    if (paymentIntent.status == StripeIntent.Status.Succeeded) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val json = gson.toJson(paymentIntent)
                        val response = Gson().fromJson(json, PaymentResponse::class.java)

                        Log.d("final", response.toString())
                        didSomething?.invoke(
                            "Payment succeeded",
                            json,
                            StripeIntent.Status.Succeeded
                        )
                    } else if (paymentIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
                        didSomething?.invoke("Payment failed",
                            paymentIntent.lastPaymentError?.message.orEmpty(), paymentIntent.status!!
                        )
                    }
                },
                onFailure = {
                    didSomething?.invoke(
                        "Error",
                        it.toString(),
                        StripeIntent.Status.Canceled
                    )
                }
            )
        }
    }

    private val _paymentIntentKey = MutableLiveData<Response<PaymentIntentModel>>()
    val paymentIntentKey get() = _paymentIntentKey

    fun createPaymentIntent(paymentMethodType: String, item: PaymentIntentModel){
        viewModelScope.launch {
            val response = repository.remoteDataSource.createPaymentIntent(paymentMethodType, item)
            _paymentIntentKey.postValue(response)
        }
    }

    /*
    Stripe payment handle section ends here
     */

    private val _adSizes = MutableLiveData<AdvertisementSize>()
    val adSizes get() = _adSizes
    var lastFetchedAdSizeData: AdvertisementSize = AdvertisementSize()
    var selectedItemPosition: Int = 0

    private fun getAdSizes(){
        viewModelScope.launch {
            val response = repository.remoteDataSource.getAdSizes()
            if(response.isSuccessful){
                val data = response.body()!!
                Log.d("response", response.body()!!.size.toString())
                _adSizes.postValue(data)
            }
        }
    }

    fun postAdvertisement(advertisement: Advertisement) {
        viewModelScope.launch {
            Log.d("upload", "ad upload called")
            val response = repository.remoteDataSource.postAdvertisement(advertisement)
            if(response.isSuccessful){
                val data = response.body()!!
                Log.d("response", "Ad upload")
                Log.d("response", data.toString())
                /*val ad = data.body!!
                val adPayment = AdvertisementPayment(
                    userId = ad.userId!!,
                    advertisementId = ad.id,
                    amount = ad.amount,
                    createdAt = ad.createdAt,
                    updatedAt = ad.updatedAt
                )*/

                //repository.remoteDataSource.postAdPayment(adPayment)
            }
            else{
                Log.d("response", "broke ${response.message()}")
            }
        }
    }


}