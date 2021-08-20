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
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentDashboardBinding
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.utils.getUserState

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding?=null
    private val binding get() = _binding!!
    private lateinit var currentUser : User
    //val viewModel: UserViewModel by activityViewModels()
    private val userViewModel : MainUserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val userState = getUserState(activity)
        if(userState.user == null){
            Log.d("userData", "Invalid request")
            activity?.finish()
        }
        /*else{
            //TODO temp solution
            // FIXME: 8/19/2021
            updateUi(userState.user)


           *//* viewModel.getUser(userState.uuid).observe(viewLifecycleOwner, {userList->
                userList.forEach { user ->
                    if(user.id == userState.uuid){
                        updateUi(user)
                        currentUser = user
                        return@forEach
                    }
                }

            })*//*

        }*/

        userViewModel.currentUser.observe(viewLifecycleOwner, { user ->
            updateUi(user)
        })






        return binding.root
    }



    private fun updateUi(it: User) {
        setTextValue(binding.dashboardUserName, it.name)
        setTextValue(binding.dashboardUserEmail, it.email)
        setTextValue(binding.userPhoneNumber, it.mobile)
        setTextValue(binding.userAddress, it.address)
        setTextValue(binding.userEmail, it.email)
        Glide.with(this)
            .load(it.image)
            .override(148, 148)
            .centerCrop()
            .into(binding.profilePicture)

    }

    private fun setTextValue(textView: TextView, value: String?){
        if(!value.isNullOrEmpty()) {
            textView.text = value
        }
    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}