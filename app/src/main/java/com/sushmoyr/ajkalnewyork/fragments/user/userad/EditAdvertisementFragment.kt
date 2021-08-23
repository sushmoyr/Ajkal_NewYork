package com.sushmoyr.ajkalnewyork.fragments.user.userad

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.drjacky.imagepicker.ImagePicker
import com.sushmoyr.ajkalnewyork.databinding.FragmentEditAdvertisementBinding
import android.provider.MediaStore.Images
import android.util.Log
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.utils.MainApplication
import com.sushmoyr.ajkalnewyork.utils.getFileName
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*





class EditAdvertisementFragment : Fragment() {

    private var _binding: FragmentEditAdvertisementBinding?=null
    private val binding get() = _binding!!
    private val args: EditAdvertisementFragmentArgs by navArgs()
    private var imageUpdated = false
    private var imageUri: Uri?=null

    private val viewModel: MainUserViewModel by activityViewModels()
    private var selectedSizePosition: Int? = null
    private val sizeArray: MutableList<String> = mutableListOf()

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri: Uri = data?.data!!
                //viewModel.uploadedImageUri.value = fileUri
                binding.adImageView.setImageURI(fileUri)
                val tempUri = imageUri
                imageUri = fileUri
                if(!imageUpdated){
                    deleteFile(tempUri!!)
                }
                imageUpdated = true
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
        // Inflate the layout for this fragment
        _binding = FragmentEditAdvertisementBinding.inflate(inflater, container, false)

        updateUi()
        setDatePicker()
        setSpinner()
        setImageUploader()
        setLoader()

        binding.upload.setOnClickListener {
            updatePost()
        }

        return binding.root
    }

    private fun updateUi() {
        val imageView = binding.adImageView
        Glide.with(this)
            .asBitmap()
            .load(args.ad.adImage)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)
                    imageUri = getImageUri(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    //adImageBitmap.recycle()
                }
            })
        binding.adTitle.setText(args.ad.adTitle)
        binding.adLink.setText(args.ad.adLink)

    }



    private fun updatePost() {
        if(validInput()){
            val contentType = "multipart/form-data".toMediaTypeOrNull()
            val userId = viewModel.currentUser.value?.id.toString().toRequestBody(contentType)
            val adTitle = binding.adTitle.text.toString().toRequestBody(contentType)
            val adLink = binding.adLink.text.toString().toRequestBody(contentType)
            val sizeId = viewModel.lastFetchedAdSizeData[selectedSizePosition!!].id.toRequestBody(contentType)
            val currentDate = LocalDateTime.now()
            val createdDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toRequestBody(contentType)
            val expDate = binding.expDate.text.toString().toRequestBody(contentType)
            val currentDateInMillis = currentDate.toInstant(ZoneOffset.UTC).toEpochMilli()
            var selectedDateInMillis = datePicker.selection
            if(viewModel.selectedDateMillis == null)
                return
            else
                selectedDateInMillis = viewModel.selectedDateMillis!!
            val diff = (selectedDateInMillis  - currentDateInMillis)
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            Log.d("forday", days.toString())
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


            updatePostAsync(
                args.ad.id,
                userId,
                adTitle,
                adLink,
                sizeId,
                adImage,
                createdDate,
                expDate,
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
                verifyInput(binding.adLink) &&
                verifyInput(binding.expDate) &&
                imageUri!=null &&
                selectedSizePosition!=null
    }

    private fun verifyInput(view: TextView): Boolean {
        return view.text.isNotEmpty()
    }


    private fun updatePostAsync(
        adId: String,
        userId: RequestBody,
        adTitle: RequestBody,
        adLink: RequestBody,
        sizeId: RequestBody,
        adImage: MultipartBody.Part,
        createdDate: RequestBody,
        expDate: RequestBody,
        forDay: RequestBody,
        amount: RequestBody,
        status: RequestBody,
        createdAt: RequestBody,
        updatedAt: RequestBody
    ){
        lifecycleScope.launch{
            viewModel.loader.value = true
            val response = viewModel
                .updateSponsoredAd(

                    adId ,userId, adTitle, adLink, sizeId, adImage, createdDate,
                    expDate, forDay, amount, status, createdAt, updatedAt
                )
            if(response.isSuccessful){
                viewModel.loader.value = false
                Toast.makeText(requireContext(), "Upload Successful", Toast.LENGTH_SHORT).show()
            }
            else{
                viewModel.loader.value = false
                Toast.makeText(requireContext(), "Upload Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }











    private val currentTimeInMillis = Calendar.getInstance().timeInMillis
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
    }

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
                ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, sizeArray)
            autoCompleteTextView.setAdapter(adapter)
            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                viewModel.selectedItemPosition = position
                selectedSizePosition = position
            }

        })
    }

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

    private fun setLoader(){
        val alert = AlertDialog.Builder(requireActivity()).setView(com.sushmoyr.ajkalnewyork.R.layout.progress_layout).create()
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

    private fun getImageUri(inImage: Bitmap): Uri? {
        val inContext = MainApplication.applicationContext()
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun deleteFile(uri: Uri){
        val file = File(uri.path!!)
        file.delete()
        if (file.exists()) {
            file.canonicalFile.delete()
            if (file.exists()) {
                MainApplication.applicationContext().deleteFile(file.name)
            }
        }
    }


}