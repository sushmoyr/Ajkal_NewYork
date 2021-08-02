package com.sushmoyr.ajkalnewyork.fragments.auth

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentLoginBinding
import com.sushmoyr.ajkalnewyork.fragments.auth.viewmodels.LoginViewModel
import com.sushmoyr.ajkalnewyork.utils.encrypt
import com.sushmoyr.ajkalnewyork.utils.observeOnce

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var emailOk: Boolean = false

    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        addEmailValidator()

        addPasswordValidator()

        binding.loginToRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailInput.text.toString()
            val password = binding.loginPasswordInput.text.toString()
            if (validInput(email, password)) {
                Log.d("login", "Email: $email and Password: $password")
                login(email, password.encrypt(password))
            }
        }

        return binding.root
    }

    private fun validInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun login(email: String, password: String) {
        val credentials = viewModel.getUser(email, password)

        credentials.observeOnce(viewLifecycleOwner, { data ->

            if (data != null) {
                var hasAccount = false

                data.forEach {
                    println(it)
                    Log.d("login", it.toString())
                    if (it.email == email && it.password == password)
                        hasAccount = true
                }

                if (hasAccount) {
                    findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                    activity?.finish()
                } else
                    showAlert("Incorrect Info", "Incorrect email or password. Try Again!!")
            } else
                showAlert("Incorrect Info", "Incorrect email or password. Try Again!!")
        })

    }

    private fun showAlert(title: String, msg: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("dismiss", null)
            .create()
        alertDialog.show()
    }

    private fun addPasswordValidator() {
        binding.loginPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null || s.isEmpty())
                    binding.loginPasswordInputLayout.error = null
                else if ((s.length < 8 || s.length > 32)) {
                    binding.loginPasswordInputLayout.error =
                        "Password must be between 8 - 32 digits"
                } else {
                    binding.loginPasswordInputLayout.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun addEmailValidator() {
        binding.loginEmailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s == null || s.isEmpty())
                    binding.loginEmailLayout.error = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.trim()
                if (s == null || s.isEmpty())
                    binding.loginEmailLayout.error = null
                else if (isValid(s)) {
                    binding.loginEmailLayout.error = null
                    emailOk = true
                } else
                    binding.loginEmailLayout.error = "Invalid Email Pattern"


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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}