package com.sushmoyr.ajkalnewyork.activities

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.transition.BitmapTransitionFactory
import com.google.gson.Gson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.CreateAdViewModel
import com.sushmoyr.ajkalnewyork.databinding.ActivityCreateAdvertisementBinding
import com.sushmoyr.ajkalnewyork.models.User
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentResponse
import kotlin.math.roundToInt

class CreateAdvertisementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAdvertisementBinding
    private val viewModel: CreateAdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertisementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUpSubscriptionPlan()

        setUploadImageFunction()

        setUpPaymentFlow()

    }

    /**
     * Stripe Payment Flow Starts Here
     */

    private lateinit var stripe: Stripe

    private fun setUpPaymentFlow() {
        stripe = Stripe(
            applicationContext,
            PaymentConfiguration.getInstance(applicationContext).publishableKey
        )

        binding.payButton.setOnClickListener {
            setViewAndChildrenEnabled(binding.mainRoot, false)
            showProgress(true)
            startCheckout()
        }

        viewModel.didSomething = { title, message, code ->
            showProgress(false)
            setViewAndChildrenEnabled(binding.mainRoot, true)
            Log.d("response", message)

            var clearData:Boolean = false
            val alertMessage: String = when(code){
                StripeIntent.Status.Succeeded -> {
                    val response = Gson().fromJson(message, PaymentResponse::class.java)
                    clearData = true
                    """
                        Status: ${response.status}
                        ID: ${response.id}
                        Client Secret: ${response.clientSecret}
                        Save this message for future use
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



    private fun startCheckout() {
        val notice = "Creating payment session"
        binding.progressText.text = notice
        val amountText = binding.cost.text.toString().drop(1)
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

    private fun setUploadImageFunction() {
        binding.adImageButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val intent = Intent()
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Image upload task
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data?.data != null) {
                // if single image is selected

                val imageUri: Uri = data.data!!
                Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show()

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver,
                        imageUri))
                } else {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                }

                Glide.with(this)
                    .asBitmap()
                    .load(bitmap)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(binding.adImageView)
            }
        }

        else{
            // Handle the result of stripe.confirmPayment
            if (stripe.isPaymentResult(requestCode, data)) {
                viewModel.doSomething(stripe, requestCode, data)
            }
        }
    }

    private fun setUpSubscriptionPlan() {
        val planArray = resources.getStringArray(R.array.subscriptions)
        val autoCompleteTextView = binding.subscriptionType
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, planArray)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val days = when(position){
                0 -> 1
                1 -> 7
                2 -> 30
                3 -> 90
                4 -> 180
                else -> 0
            }
            val cost = days * 4.99
            val displayText = "$$cost"
            binding.cost.text = displayText
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
        binding.adTitle.text?.clear()
        binding.adLink.text?.clear()
        binding.adImageView.setImageURI(null)
        binding.subscriptionType.clearListSelection()
        binding.cardInputWidget.clear()
    }
}