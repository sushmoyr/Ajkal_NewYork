package com.sushmoyr.ajkalnewyork.fragments.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentChangePasswordBinding
import com.sushmoyr.ajkalnewyork.models.User
import com.sushmoyr.ajkalnewyork.utils.encrypt
import com.sushmoyr.ajkalnewyork.utils.getUserState

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding ?= null
    private val binding get() = _binding!!

    private lateinit var currentUser : User
    val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

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

        binding.updatePasswordButton.setOnClickListener {
            updatePassword()
        }

        return binding.root
    }

    private fun updatePassword() {
        val currentPassword = currentUser.password
        val currentPassInput = binding.updateCurrentPassword.text.toString()
        val newPass = binding.newPass.text.toString()
        val confirmPass = binding.confirmNewPass.text.toString()
        if(currentPassword != currentPassInput.encrypt(currentPassInput) || newPass != confirmPass){
            Toast.makeText(requireContext(), "Password didn't match", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            val user = User(
                currentUser.id,
                currentUser.fullName,
                currentUser.email,
                newPass.encrypt(newPass),
                currentUser.address,
                currentUser.phoneNo,
                currentUser.profilePhoto
            )


            viewModel.updateUser(user)
            Toast.makeText(requireContext(), "Password Changed Successfully", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateUi(user: User) {
        binding.dashboardUserName.text = user.fullName
        Glide.with(this)
            .load(user.profilePhoto)
            .override(148, 148)
            .centerCrop()
            .into(binding.profilePicture)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}