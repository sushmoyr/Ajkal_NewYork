package com.sushmoyr.ajkalnewyork.fragments.user.userinfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentDashboardBinding
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.utils.getUserState

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding?=null
    private val binding get() = _binding!!
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
            .placeholder(R.drawable.ic_placeholder)
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