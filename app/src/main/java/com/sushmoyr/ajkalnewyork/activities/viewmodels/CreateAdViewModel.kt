package com.sushmoyr.ajkalnewyork.activities.viewmodels

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stripe.android.Stripe
import com.stripe.android.getPaymentIntentResult
import com.stripe.android.model.StripeIntent
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentResponse
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class CreateAdViewModel: ViewModel() {

    val repository = Repository()

    var didSomething: ((title: String,
                        message: String,
                        code: StripeIntent.Status) -> Unit) ? = null

    fun doSomething(stripe: Stripe, requestCode: Int, data: Intent?) {
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

}