package com.sushmoyr.ajkalnewyork.fragments.user.userad

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentUploadAdBinding
import com.sushmoyr.ajkalnewyork.utils.MainApplication
import com.sushmoyr.ajkalnewyork.utils.getFileName
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class UploadAdFragment : Fragment() {

    private var _binding: FragmentUploadAdBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainUserViewModel by activityViewModels()
    private var selectedSizePosition: Int? = null
    private val sizeArray: MutableList<String> = mutableListOf()
    
    private var imageUri: Uri? = null
    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri: Uri = data?.data!!
                //viewModel.uploadedImageUri.value = fileUri
                binding.adImageView.setImageURI(fileUri)
                imageUri = fileUri
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadAdBinding.inflate(inflater, container, false)

        //setDatePicker()
        setSpinner()
        setImageUploader()
        setLoader()
        
        binding.upload.setOnClickListener {
            uploadPost()
        }
        

        return binding.root
    }

    /**
     * Ad upload Area
     */

    private fun uploadPost() {
        if(validInput()){
            val contentType = "multipart/form-data".toMediaTypeOrNull()
            val userId = viewModel.currentUser.value?.id.toString().toRequestBody(contentType)
            val adTitle = binding.adTitle.text.toString().toRequestBody(contentType)
            val sizeId = viewModel.lastFetchedAdSizeData[selectedSizePosition!!].id.toRequestBody(contentType)
            val currentDate = LocalDateTime.now()
            val createdDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toRequestBody(contentType)
            val days = binding.expDate.text.toString().toInt()
            val forDay = days.toString().toRequestBody(contentType)

            val bill = viewModel
                .lastFetchedAdSizeData[selectedSizePosition!!].amount.toDouble()

            val cost = bill * days
            val amount = cost.toString().toRequestBody(contentType)

            val context = MainApplication.applicationContext()
            val parcelFileDescriptor =
                context.contentResolver.openFileDescriptor(imageUri!!,
                    "r", null)!!
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(context.cacheDir, context.contentResolver.getFileName(imageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            val adImage = MultipartBody.Part.createFormData(
                "ad_image",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )

            Log.d("upload", file.name)

            val createdAt = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .toRequestBody(contentType)
            val updatedAt = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .toRequestBody(contentType)
            val status = "0".toRequestBody(contentType)

            uploadPostAsync(
                userId,
                adTitle,
                sizeId,
                adImage,
                createdDate,
                forDay,
                amount,
                status,
                createdAt,
                updatedAt
            )
        }
        else{
            Toast.makeText(requireContext(), "All fields are required!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validInput(): Boolean {
        return verifyInput(binding.adTitle) &&
                verifyInput(binding.expDate) &&
                imageUri!=null &&
                selectedSizePosition!=null &&
                TextUtils.isDigitsOnly(binding.expDate.text)
    }

    private fun verifyInput(view: TextView): Boolean {
        return view.text.isNotEmpty()
    }

    private fun uploadPostAsync(
        userId: RequestBody,
        adTitle: RequestBody,
        sizeId: RequestBody,
        adImage: MultipartBody.Part,
        createdDate: RequestBody,
        forDay: RequestBody,
        amount: RequestBody,
        status: RequestBody,
        createdAt: RequestBody,
        updatedAt: RequestBody
    ){
        lifecycleScope.launch{
            viewModel.loader.value = true
            val response = viewModel
                .uploadSponsoredAd(
                    userId,
                    adTitle,
                    sizeId,
                    adImage,
                    createdDate,
                    forDay,
                    amount,
                    status,
                    createdAt,
                    updatedAt
                )
            if(response.isSuccessful){
                viewModel.loader.value = false
                Toast.makeText(requireContext(), "Upload Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            else{
                viewModel.loader.value = false
                Toast.makeText(requireContext(), "Upload Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Image Upload Area
     */

    private fun setImageUploader() {
        binding.adImageButton.setOnClickListener{
            /*val intent = ImagePicker.with(requireActivity())
                .galleryOnly()
                .createIntent()
            intent.apply {
                action = Intent.ACTION_OPEN_DOCUMENT
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }*/
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startForProfileImageResult.launch(intent)
        }
    }

    /**
     * Ui handling area
     */

    private fun setSpinner() {
        viewModel.adSizes.observe(viewLifecycleOwner, { adSizes ->
            viewModel.lastFetchedAdSizeData = adSizes
            sizeArray.clear()
            adSizes.forEach { item ->
                val content = "${item.name} (W: ${item.width}, H: ${item.height}) \n$${item.amount}"
                sizeArray.add(content)
            }

            sizeArray.forEach {
                print(it)
            }

            val autoCompleteTextView = binding.subscriptionType
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, sizeArray)
            autoCompleteTextView.setAdapter(adapter)
            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                viewModel.selectedItemPosition = position
                selectedSizePosition = position
            }

        })
    }

    /*private val currentTimeInMillis = Calendar.getInstance().timeInMillis
    private val constraintsBuilder =
        CalendarConstraints.Builder()
            .setStart(currentTimeInMillis)
            .build()

    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Expire date")
        .setSelection(Calendar.getInstance().timeInMillis)
        .setCalendarConstraints(constraintsBuilder)
        .build()

    private fun setDatePicker() {
        val datePickerButton = binding.expDate

        datePickerButton.setOnClickListener {
            datePicker.show(activity?.supportFragmentManager!!, "DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener { currentSelectedDate ->
            viewModel.selectedDateMillis = currentSelectedDate
            val dateTime: LocalDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(currentSelectedDate),
                ZoneId.systemDefault()
            )
            val dateAsFormattedText: String =
                dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            binding.expDate.setText(dateAsFormattedText)
        }
    }*/

    private fun setLoader(){
        val alert = AlertDialog.Builder(requireActivity()).setView(R.layout.progress_layout).create()
        alert.setCanceledOnTouchOutside(false)

        viewModel.loader.observe(viewLifecycleOwner, {
            if(it){
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                alert.show()
            }
            else{
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                alert.dismiss()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}