package com.sushmoyr.ajkalnewyork.fragments.user

import android.app.Activity
import android.content.Intent
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
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.databinding.FragmentEditProfileBinding
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.utils.getUserState

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding ?= null
    private val binding get() = _binding!!

    private lateinit var currentUser : User
    val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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
            /*viewModel.getUser(userState.uuid).observe(viewLifecycleOwner, {userList->
                userList.forEach { user ->
                    if(user.id == userState.uuid){
                        updateUi(user)
                        currentUser = user
                        return@forEach
                    }
                }

            })*/

        }
        binding.updateProfilePic.setOnClickListener {
            uploadImage()
        }

        binding.updateProfileButton.setOnClickListener {
            //updateProfile()
            // FIXME: 8/19/2021
            //TODO FIXME
        }

        return binding.root
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

    //TODO FIXME
    /*private fun updateProfile() {
        val user = InvalidUser(
            currentUser.id,
            binding.updateFullName.text.toString(),
            binding.updateEmail.text.toString(),
            currentUser.password,
            binding.updateAddress.text.toString(),
            binding.updatePhone.text.toString(),
            currentUser.profilePhoto
        )

        viewModel.updateUser(user)
        Toast.makeText(requireContext(), "InvalidUser Updated", Toast.LENGTH_SHORT).show()
    }*/

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

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data?.data != null) {
                // if single image is selected

                val imageUri: Uri = data.data!!

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, imageUri))
                } else {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                }
                /*currentUser = InvalidUser(currentUser.id, currentUser.fullName, currentUser.email, currentUser
                    .password, currentUser.address, currentUser.phoneNo, bitmap)

                viewModel.updateUser(currentUser)*/
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}