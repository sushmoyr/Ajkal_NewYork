package com.sushmoyr.ajkalnewyork.fragments.auth

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentRegistrationBinding
import com.sushmoyr.ajkalnewyork.fragments.auth.viewmodels.RegisterViewModel
import com.sushmoyr.ajkalnewyork.models.User
import com.sushmoyr.ajkalnewyork.models.utility.RegisterRequest
import com.sushmoyr.ajkalnewyork.utils.encrypt
import kotlinx.coroutines.launch
import java.util.*

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private var emailOk = false
    private var passwordOk = false

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        setEmailValidator()
        setPasswordValidator()
        setPasswordMatcher()
        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.registerToLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
        binding.registrationButton.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() {
        val name = binding.fullNameInput.text.toString()
        val email = binding.regEmailInput.text.toString()
        val password = binding.regPasswordInput.text.toString()
        val confirmPass = binding.regConfirmPasswordInput.text.toString()

        val requiredMsg = "Required Field"

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()){
            Toast.makeText(requireContext(), "Complete all fields", Toast.LENGTH_SHORT).show()
        }
        else if(!passwordOk)
            Toast.makeText(requireContext(), "Check your password again", Toast.LENGTH_SHORT).show()
        else if(!emailOk)
            Toast.makeText(requireContext(), "Check your email again", Toast.LENGTH_SHORT).show()
        else{
            val uuid: String = UUID.randomUUID().toString()
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.profile_placeholder)
            if(bitmap == null){
                Toast.makeText(requireContext(), "Bitmap null", Toast.LENGTH_SHORT).show()
            }
            val user = User(uuid,name, email, password.encrypt(password), profilePhoto = bitmap!!)
            Log.d("reg", user.toString())
            //confirmRegistration(user)
            registerWithApi(name, email, password, confirmPass)
        }
    }

    private fun registerWithApi(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ) {
        val request = RegisterRequest(name, email, password, passwordConfirmation)
        lifecycleScope.launch {
            setViewAndChildrenEnabled(binding.root, false)
            val response = viewModel.register(request)
            if(response.isSuccessful){
                setViewAndChildrenEnabled(binding.root, true)
                when(response.code()){
                    200 -> {
                        Toast.makeText(requireContext(), "Success!! Login now", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                    }
                    202 -> {
                        Toast.makeText(
                            requireContext(),
                            "Email already exists!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun confirmRegistration(user: User) {
        lifecycleScope.launch {
            val email = viewModel.getEmail(user.email)
            email.observe(viewLifecycleOwner, { data ->
                if (data == null) {
                    register(user)
                } else {
                    var hasEmail = false
                    data.forEach { item ->
                        if (item.email == user.email) {
                            hasEmail = true
                        }
                    }
                    if (!hasEmail)
                        register(user)
                    else
                        Toast.makeText(requireContext(), "Email already exists", Toast.LENGTH_SHORT)
                            .show()
                }
            })
        }
    }

    private fun register(user: User) {
        viewModel.addUser(user)
        Toast.makeText(requireContext(), "Success!! Login now", Toast.LENGTH_SHORT)
            .show()
        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun setPasswordMatcher() {
        val confirmPassLayout = binding.confirmPasswordLayout

        binding.regConfirmPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val password = binding.regPasswordInput.text?.toString()
                Log.d("Validator", password.toString())

                if (password.isNullOrEmpty()) {
                    confirmPassLayout.error = "Set a password first!!"
                    passwordOk = false
                } else if (password != s.toString()) {
                    confirmPassLayout.error = "Password didn't match!!"
                    passwordOk = false
                } else {
                    confirmPassLayout.error = null
                    passwordOk = true
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setPasswordValidator() {
        binding.regPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null || s.isEmpty())
                    binding.enterPasswordLayout.error = null
                else if ((s.length < 8 || s.length > 32)) {
                    binding.enterPasswordLayout.error = "Password must be between 8 - 32 digits"
                } else {
                    binding.enterPasswordLayout.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setEmailValidator() {
        binding.regEmailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s == null || s.isEmpty()) {
                    binding.enterEmailLayoutReg.error = null
                    emailOk = false
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.trim()
                if (s == null || s.isEmpty()) {
                    binding.enterEmailLayoutReg.error = null
                } else if (isValid(s)) {
                    binding.enterEmailLayoutReg.error = null
                    emailOk = true
                } else {
                    binding.enterEmailLayoutReg.error = "Invalid Email Pattern"
                    emailOk = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

            fun isValid(text: CharSequence): Boolean {
                return if (TextUtils.isEmpty(text))
                    false
                else android.util.Patterns.EMAIL_ADDRESS.matcher(text.trim()).matches()
            }


        })
    }

    private fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            val viewGroup = view as ViewGroup
            for (i in 0 until viewGroup.childCount) {
                val child: View = viewGroup.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
    }

}


