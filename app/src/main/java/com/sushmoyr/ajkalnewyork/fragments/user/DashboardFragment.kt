package com.sushmoyr.ajkalnewyork.fragments.user

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sushmoyr.ajkalnewyork.databinding.FragmentDashboardBinding
import com.sushmoyr.ajkalnewyork.models.User
import com.sushmoyr.ajkalnewyork.utils.getUserState

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding?=null
    private val binding get() = _binding!!
    private lateinit var currentUser : User
    val viewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val userState = getUserState(activity)
        if(userState.uuid == null){
            Log.d("userData", "Invalid request")
            activity?.finish()
        }
        else{
            viewModel.getUser(userState.uuid).observe(viewLifecycleOwner, {userList->
                userList.forEach { user ->
                    if(user.id == userState.uuid){
                        updateUi(user)
                        currentUser = user
                        return@forEach
                    }
                }

            })

        }

        binding.updateProfilePic.setOnClickListener {
            uploadImage()
        }


        return binding.root
    }

    private fun uploadImage() {
        val intent = Intent()
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }



    private fun updateUi(it: User) {
        binding.dashboardUserName.text = it.fullName
        binding.dashboardUserEmail.text = it.email
        binding.userPhoneNumber.text = it.phoneNo
        binding.userAddress.text = it.address
        binding.userEmail.text = it.email
        binding.profilePicture.setImageBitmap(it.profilePhoto)
        Glide.with(this)
            .load(it.profilePhoto)
            .override(148, 148)
            .centerCrop()
            .into(binding.profilePicture)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data?.data != null) {
                // if single image is selected

                val imageUri: Uri = data.data!!

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, imageUri))
                } else {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                }
                currentUser = User(currentUser.id, currentUser.fullName, currentUser.email, currentUser
                    .password, currentUser.address, currentUser.phoneNo, bitmap)

                viewModel.updateUser(currentUser)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}