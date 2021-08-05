package com.sushmoyr.ajkalnewyork.utils

import android.app.Application
import android.content.Context
import com.stripe.android.PaymentConfiguration
import com.sushmoyr.ajkalnewyork.utils.Constants.STRIPE_PUBLISHABLE_KEY

class MainApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = MainApplication.applicationContext()

        PaymentConfiguration.init(
            applicationContext,
            STRIPE_PUBLISHABLE_KEY
        )
    }

}