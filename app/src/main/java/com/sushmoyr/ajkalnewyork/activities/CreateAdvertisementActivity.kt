package com.sushmoyr.ajkalnewyork.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.google.gson.Gson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.CreateAdViewModel
import com.sushmoyr.ajkalnewyork.databinding.ActivityCreateAdvertisementBinding
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentResponse
import com.sushmoyr.ajkalnewyork.models.utility.HeartLandPaymentModel
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.repository.Repository
import com.sushmoyr.ajkalnewyork.utils.getUserState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CreateAdvertisementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAdvertisementBinding
    private val viewModel: CreateAdViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var selectedSizePosition: Int? = null
    private val args: CreateAdvertisementActivityArgs by navArgs()
    private lateinit var user: User
    private val repository = Repository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertisementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //val ad = args.ad
        val amount = args.ad.amount
        user = getUserState(this).user!!

        binding.payButton.setOnClickListener {
            /*if(verify()){
                setViewAndChildrenEnabled(binding.mainRoot, false)
                showProgress(true)
                startCheckout(amount)
            }*/
            if(verify())
                showAlert()
        }

        setUpPaymentFlow()

    }

    private fun showAlert(){
        var selectedPaymentMethod = 0
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Choose Payment Method")
        alert.setSingleChoiceItems(R.array.payment_methods, 0
        ) { dialog, which ->
            selectedPaymentMethod = which
        }
        alert.setPositiveButton("Continue") { dialog, which ->
            if(selectedPaymentMethod==0){
                startStripCheckout(args.ad.amount)
                Log.d("payment", "Striping")
            } else {
                startHeartLandCheckout()
                Log.d("payment", "Heartless")
            }
        }
        alert.create()
        alert.show()
    }

    private fun startHeartLandCheckout() {
        val data = binding.cardInputWidget.paymentMethodCard?.toParamMap() ?: return
        val cardNumber = data["number"].toString()
        val cardExpMonth = data["exp_month"].toString()
        val cardExpYear = data["exp_year"].toString()
        val cvc = data["cvc"].toString()


        val model = HeartLandPaymentModel(args.ad.id, user.id.toString(), user.email, user.name,
            user.mobile, cardExpMonth, cardExpYear, cardNumber, cvc)

        println(model)

        lifecycleScope.launch {
            setViewAndChildrenEnabled(binding.mainRoot, false)
            showProgress(true)
            binding.progressText.text = "Submitting Payment Info"
            val locationDef = async { repository.remoteDataSource.getSessionGeoLocationInfo()}
            val session = locationDef.await().body()!!
            model.setSessionInfo(session)
            val response = viewModel.handleHeartland(model)
            if(response.isSuccessful){
                val body = response.body()!!
                showAlert(body.message)
                setViewAndChildrenEnabled(binding.mainRoot, false)
            }
            showProgress(false)
            setViewAndChildrenEnabled(binding.mainRoot, true)
        }
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


    private fun startStripCheckout(amountText: String) {
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
            for (i in 0 until view.childCount) {
                val child: View = view.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
    }

    private fun showAlert(message: String){
        val alert = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("Dismiss", null)

        alert.setPositiveButton("Ok"){dialog, which ->
            onBackPressed()
        }
        alert.setNegativeButton("Cancel", null)

        alert.create().show()

    }


    private fun clearFields() {
        onBackPressed()
        binding.cardInputWidget.clear()
    }
}