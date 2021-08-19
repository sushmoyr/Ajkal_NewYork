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
        val user = User(
            currentUser.id,
            binding.updateFullName.text.toString(),
            binding.updateEmail.text.toString(),
            currentUser.password,
            binding.updateAddress.text.toString(),
            binding.updatePhone.text.toString(),
            currentUser.profilePhoto
        )

        viewModel.updateUser(user)
        Toast.makeText(requireContext(), "User Updated", Toast.LENGTH_SHORT).show()
    }*/

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}