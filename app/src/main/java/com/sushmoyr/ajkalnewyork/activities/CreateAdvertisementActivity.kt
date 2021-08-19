package com.sushmoyr.ajkalnewyork.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.google.gson.Gson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.sushmoyr.ajkalnewyork.activities.viewmodels.CreateAdViewModel
import com.sushmoyr.ajkalnewyork.databinding.ActivityCreateAdvertisementBinding
import com.sushmoyr.ajkalnewyork.datasource.api.RetrofitInstance
import com.sushmoyr.ajkalnewyork.models.UploadResponse
import com.sushmoyr.ajkalnewyork.models.core.Advertisement
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

class CreateAdvertisementActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    private lateinit var binding: ActivityCreateAdvertisementBinding
    private val viewModel: CreateAdViewModel by viewModels()
    private var selectedImageUri: Uri? = null

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

    private fun verify(): Boolean {
        return when {
            selectedImageUri == null -> {
                Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show()
                false
            }
            binding.adTitle.text.isNullOrBlank() -> {
                Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show()
                false
            }
            viewModel.totalCost.value == 0.0 -> {
                Toast.makeText(this, "Select a plan", Toast.LENGTH_SHORT).show()
                false
            }
            else -> binding.cardInputWidget.validateAllFields()
        }
    }

    /**
     * Stripe Payment Flow Starts Here
     */

    private lateinit var stripe: Stripe

    private fun setUpPaymentFlow() {


        binding.payButton.setOnClickListener {
            if(verify()){
                setViewAndChildrenEnabled(binding.mainRoot, false)
                showProgress(true)
                startCheckout()
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

                    handleAdvertisement(response)

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


    private fun startCheckout() {
        stripe = Stripe(
            applicationContext,
            PaymentConfiguration.getInstance(applicationContext).publishableKey
        )
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
                selectedImageUri = imageUri
                Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show()

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver,
                        imageUri))
                } else {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                }

                Glide.with(this)
                    .load(imageUri)
                    .into(binding.adImageView)
            }
        }

        else{
            // Handle the result of stripe.confirmPayment
            if (stripe.isPaymentResult(requestCode, data)) {
                viewModel.handleStripe(stripe, requestCode, data)
            }
        }
    }

    private fun setUpSubscriptionPlan() {

        binding.adLimit.setText("1")

        var cost: Double = 0.0
        var limit: Int = 1

        val sizeArray: MutableList<String> = mutableListOf()

        viewModel.adSizes.observe(this,{adSizes ->
            viewModel.lastFetchedAdSizeData = adSizes
            adSizes.forEach { item ->
                val content = "${item.name} (W: ${item.width}, H: ${item.height}) \n$${item.amount}"
                sizeArray.add(content)
            }

            sizeArray.forEach {
                print(it)
            }

            val autoCompleteTextView = binding.subscriptionType
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sizeArray)
            autoCompleteTextView.setAdapter(adapter)
            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                cost = adSizes[position].amount.toDouble()
                viewModel.seTotalCost(cost, limit)
                viewModel.selectedItemPosition = position
            }

            binding.adLimit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.isNullOrBlank()){
                        limit = try {
                            s.toString().toInt()
                        } catch (e: IllegalArgumentException){
                            1
                        }

                        viewModel.seTotalCost(cost, limit)
                    }
                    else {
                        limit = 1
                        viewModel.seTotalCost(cost, limit)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            viewModel.totalCost.observe(this, {
                val value = "$$it"
                binding.cost.text = value
            })

        })
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

    private fun getAdvertisement(paymentId: String, amount: String, adImage: String):
            Advertisement {
        val userId = getUserState(this).user?.id.toString()
        val adTitle = binding.adTitle.text.toString()
        val adLink = binding.adLink.text.toString()
        val dateTime = LocalDateTime.now()
        val createdAt = dateTime.toString()
        val sizeId = viewModel.lastFetchedAdSizeData[viewModel.selectedItemPosition].id
        val forDay = binding.adLimit.text.toString()
        val expDate = dateTime.plusDays(forDay.toLong()).toString()

        return Advertisement(
            userId = userId,
            adTitle = adTitle,
            adLink = adLink,
            _adImage = adImage,
            expDate = expDate,
            createdAt = createdAt,
            sizeId = sizeId,
            forDay = forDay,
            amount = amount,
            paymentId = paymentId
        )
    }

    private fun postAdvertisement(response: PaymentResponse, image: String) {
        Log.d("upload", "post ad called with image: $image")
        val advertisement = getAdvertisement(response.id, (response.amount /100).toString(), image)
        viewModel.postAdvertisement(advertisement)
    }

    private fun postImage(paymentResponse: PaymentResponse) {

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        //progress_bar.progress = 0
        val body = UploadRequestBody(file, "image", this)

        lifecycleScope.launch {
            RetrofitInstance.api.uploadImage(
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    body
                ),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), getLastSize())
            ).enqueue(object : Callback<UploadResponse> {
                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Log.d("upload", "failure")
                    postAdvertisement(paymentResponse, "null")
                }

                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    response.body()?.let {
                        Log.d("upload", "response: ${it.image}")
                        postAdvertisement(paymentResponse, it.image)
                        Log.d("upload", "response: ${it.message}")
                    }
                }
            })
        }

    }

    private fun getLastSize(): String {
        return viewModel.lastFetchedAdSizeData[viewModel.selectedItemPosition].id
    }

    private fun handleAdvertisement(response: PaymentResponse) {

        postImage(response)
    }

    private fun clearFields() {
        onBackPressed()
        binding.adTitle.text?.clear()
        binding.adLink.text?.clear()
        binding.adImageView.setImageURI(null)
        binding.subscriptionType.clearListSelection()
        binding.cardInputWidget.clear()
    }

    override fun onProgressUpdate(percentage: Int) {
        Log.d("upload", percentage.toString())
    }
}