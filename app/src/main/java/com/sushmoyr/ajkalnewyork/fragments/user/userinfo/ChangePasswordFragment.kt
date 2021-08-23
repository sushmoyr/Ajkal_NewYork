package com.sushmoyr.ajkalnewyork.fragments.user.userinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentChangePasswordBinding
import com.sushmoyr.ajkalnewyork.models.utility.UpdatePasswordRequest
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.utils.Constants.UPDATE_PASSWORD_OK
import com.sushmoyr.ajkalnewyork.utils.getUserState
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        val userState = getUserState(activity)

        if (userState.user == null) {
            Log.d("userData", "Invalid request")
            activity?.finish()
        } else {
            updateUi(userState.user)
        }

        binding.updatePasswordButton.setOnClickListener {
            updatePassword(userState.user!!.id.toString())
        }

        setLoader()

        return binding.root
    }

    private fun updatePassword(id: String) {
        val currentPass = binding.updateCurrentPassword.text.toString()
        val newPass = binding.newPass.text.toString()
        val confirmPass = binding.confirmNewPass.text.toString()
        if (newPass != confirmPass) {
            Toast.makeText(requireContext(), "Password didn't match", Toast.LENGTH_SHORT).show()
            return
        } else {
            viewModel.setUpdatePasswordLoadingState(true)
            val request = UpdatePasswordRequest(
                newPass,
                confirmPass,
                currentPass,
            )

            lifecycleScope.launch {
                val response = viewModel.updatePassword(id, request)
                if(response.isSuccessful){
                    viewModel.setUpdatePasswordLoadingState(false)
                    val message = response.body()!!.message
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    if(message==UPDATE_PASSWORD_OK){
                        findNavController().navigateUp()
                    }
                }
                else{
                    viewModel.setUpdatePasswordLoadingState(false)
                    Toast.makeText(requireContext(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUi(user: User) {
        binding.dashboardUserName.text = user.name
        Glide.with(this)
            .load(user.image)
            .override(148, 148)
            .centerCrop()
            .into(binding.profilePicture)

    }

    private fun setLoader(){
        val alert = AlertDialog.Builder(requireActivity()).setView(R.layout.progress_layout).create()
        alert.setCanceledOnTouchOutside(false)

        viewModel.updatePasswordLoader.observe(viewLifecycleOwner, {
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