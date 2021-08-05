package com.sushmoyr.ajkalnewyork.datasource.api

import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StripeApi {
    @POST("create-payment-intent")
    suspend fun createPaymentIntent(
        @Body body: PaymentIntentModel
    ): Response<PaymentIntentModel>
}