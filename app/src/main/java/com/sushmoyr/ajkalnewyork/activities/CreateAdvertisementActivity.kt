package com.sushmoyr.ajkalnewyork.activities

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.google.gson.Gson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.sushmoyr.ajkalnewyork.activities.viewmodels.CreateAdViewModel
import com.sushmoyr.ajkalnewyork.databinding.ActivityCreateAdvertisementBinding
import com.sushmoyr.ajkalnewyork.datasource.api.RetrofitInstance
import com.sushmoyr.ajkalnewyork.models.UploadResponse
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentResponse
import com.sushmoyr.ajkalnewyork.utils.UploadRequestBody
import com.sushmoyr.ajkalnewyork.utils.getFileName
import com.sushmoyr.ajkalnewyork.utils.getUserState
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import kotlin.math.roundToInt

class CreateAdvertisementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAdvertisementBinding
    private val viewModel: CreateAdViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var selectedSizePosition: Int? = null
    private val args: CreateAdvertisementActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertisementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //val ad = args.ad
        val amount = args.ad.amount

        setUpPaymentFlow()

    }

    private fun verify(): Boolean {
        return binding.cardInputWidget.validateAllFields()
    }

    /**
     * Stripe Payment Flow Starts Here
     */

    private lateinit var stripe: Stripe

    private fun setUpPaymentFlow() {

        val amount = args.ad.amount

        binding.payButton.setOnClickListener {
            if(verify()){
                setViewAndChildrenEnabled(binding.mainRoot, false)
                showProgress(true)
                startCheckout(amount)
            }

        }

        viewModel.didSomething = { title, message, code ->
            showProgress(false)
            setViewAndChildrenEnabled(binding.mainRoot, true)
            Log.d("response", message)

            var clearData:Boolean = false
            val alertMessage: String = when(code){
                StripeIntent.Status.Succeeded -> {
                    Log.d("response", message)
                    val response = Gson().fromJson(message, PaymentResponse::class.java)

                    clearData = true
                    """
                        Status: ${response.status}
                        ID: ${response.id}
                        Client Secret: ${response.clientSecret}
                        Save this message for future use
                        **Your advertisement will be posted
                    """.trimIndent()
                }
                else -> message
            }

            val alert = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertMessage)
                .setNegativeButton("Dismiss", null)

            if(clearData){
                alert.setPositiveButton("Ok"){_, _ ->
                    clearFields()
                }
            }

            alert.create().show()

        }
    }


    private fun startCheckout(amountText: String) {
        stripe = Stripe(
            applicationContext,
            PaymentConfiguration.getInstance(applicationContext).publishableKey
        )
        val notice = "Creating payment session"
        binding.progressText.text = notice
        Log.d("stripeSetup", amountText)
        val amount = ((amountText.toDouble()) * 100.0).roundToInt()
        val item = PaymentIntentModel(amount)

        viewModel.createPaymentIntent("card", item)

        viewModel.paymentIntentKey.observe(this, {
            if (it.isSuccessful && it.body() != null) {
                //continue with the payment
                val response = it.body()!!


                val key = response.clientSecret!!
                val cardInput = binding.cardInputWidget
                val params = cardInput.paymentMethodCreateParams
                if (params != null) {
                    val confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, key)
                    val anotherNotice = "Start transaction"
                    binding.progressText.text = anotherNotice
                    stripe.confirmPayment(this, confirmParams)
                }
            }
        })

    }

    private fun showProgress(isVisible: Boolean){
        if(isVisible){
            binding.progressView.visibility = View.VISIBLE
        }
        else{
            binding.progressView.visibility = View.GONE
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle the result of stripe.confirmPayment
        if (stripe.isPaymentResult(requestCode, data)) {
            viewModel.handleStripe(stripe, requestCode, data, args.ad)
        }
    }



    private fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            val viewGroup = view as ViewGroup
            for (i in 0 until viewGroup.childCount) {
                val child: View = viewGroup.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
    }


    private fun clearFields() {
        onBackPressed()
        binding.cardInputWidget.clear()
    }
}