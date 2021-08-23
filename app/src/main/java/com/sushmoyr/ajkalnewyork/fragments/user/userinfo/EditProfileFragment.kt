package com.sushmoyr.ajkalnewyork.fragments.user.userinfo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.google.gson.Gson
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentEditProfileBinding
import com.sushmoyr.ajkalnewyork.fragments.user.UserStateViewModel
import com.sushmoyr.ajkalnewyork.models.UserState
import com.sushmoyr.ajkalnewyork.models.utility.ProfileUpdateRequest
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.utils.Constants
import com.sushmoyr.ajkalnewyork.utils.MainApplication
import com.sushmoyr.ajkalnewyork.utils.getFileName
import com.sushmoyr.ajkalnewyork.utils.getUserState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding ?= null
    private val binding get() = _binding!!

    private lateinit var currentUser : User
    private val viewModel: UserViewModel by activityViewModels()
    private val userStateViewModel: UserStateViewModel by activityViewModels()

    private var imageUri:Uri ?= null
    private var imageUpdated:Boolean = false

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri: Uri = data?.data!!
                //viewModel.uploadedImageUri.value = fileUri
                binding.profilePicture.setImageURI(fileUri)
                imageUri = fileUri
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
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        val userState = getUserState(activity)

        if(userState.user == null){
            Log.d("userData", "Invalid request")
            activity?.finish()
        }
        else{
            //TODO temp solution
            // FIXME: 8/19/2021
            updateUi(userState.user)
        }

        setLoader()

        binding.updateProfilePic.setOnClickListener {
            uploadImage()
        }

        binding.updateProfileButton.setOnClickListener {
            updateProfile()
        }

        return binding.root
    }

    private fun updateProfile() {
        viewModel.setLoadingState(true)
        val requestBody = ProfileUpdateRequest(
            binding.updateFullName.text.toString(),
            binding.updateEmail.text.toString(),
            binding.updatePhone.text.toString(),
            binding.updateAddress.text.toString()
        )

        val user = getUserState(requireActivity())
        var file: File? = null

        if(user.user==null)
            return

        if(imageUpdated){
            file = getImageFile()
        }

        val loggedInUser = user.user!!
        val id = loggedInUser.id

        lifecycleScope.launch{

            if(file!=null){
                val profileImage = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )

                val response = viewModel.uploadProfileImage(id, profileImage)
                if(response.isSuccessful){
                    Toast.makeText(requireContext(), "Image Updated", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(
                        requireContext(),
                        "Error while uploading image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            val response = viewModel.updateUserByApi(id.toString(), requestBody)
            if(response.isSuccessful){
                viewModel.setLoadingState(false)
                userStateViewModel.updateUserStateFromNetwork(id.toString())
                Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            else {
                viewModel.setLoadingState(false)
                Toast.makeText(requireContext(), "Profile update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getImageFile(): File {
        val context = MainApplication.applicationContext()
        val parcelFileDescriptor =
            context.contentResolver.openFileDescriptor(imageUri!!,
                "r", null)!!
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(context.cacheDir, context.contentResolver.getFileName(imageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    private fun updateSharedPref(loggedInUser: User, requestBody: ProfileUpdateRequest) {

        val user = loggedInUser.copy(
            name = requestBody.name,
            email = requestBody.email,
            mobile = requestBody.mobile,
            address = requestBody.address
        )

        val userState = UserState(true, user)
        val gson = Gson()
        val value = gson.toJson(userState)
        Log.d("userState", value)
        val sharedPref = activity?.getSharedPreferences(
            Constants.USER_AUTHENTICATION_KEY, Context
            .MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString(Constants.USER_AUTHENTICATION_STATE_KEY, value)
            apply()
        }
    }

    private fun updateUi(user: User) {
        binding.updateFullName.setText(user.name)
        binding.updateEmail.setText(user.email)
        binding.updateAddress.setText(user.address)
        binding.updatePhone.setText(user.mobile)
        binding.dashboardUserName.text = user.name
        Glide.with(this)
            .load(user.image)
            .override(148, 148)
            .centerCrop()
            .into(binding.profilePicture)

    }


    private fun uploadImage() {

        val intent = Intent(Intent.ACTION_PICK)
        //intent.addCategory(Intent.CATEGORY_APP_GALLERY)
        intent.type = "image/*"
        //intent.action = Intent.ACTION_GET_CONTENT
        startForProfileImageResult.launch(intent)
    }

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