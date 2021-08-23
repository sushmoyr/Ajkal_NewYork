package com.sushmoyr.ajkalnewyork.fragments.auth.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentLoginBinding
import com.sushmoyr.ajkalnewyork.fragments.auth.viewmodels.LoginViewModel
import com.sushmoyr.ajkalnewyork.models.UserState
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.utils.Constants.USER_AUTHENTICATION_KEY
import com.sushmoyr.ajkalnewyork.utils.Constants.USER_AUTHENTICATION_STATE_KEY
import com.sushmoyr.ajkalnewyork.utils.observeOnce
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var emailOk: Boolean = false

    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        addEmailValidator()

        addPasswordValidator()

        setLoader()

        binding.googleLoginButton.setOnClickListener{
            lifecycleScope.launch {
                viewModel.logout()
            }
        }

        binding.loginToRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailInput.text.toString()
            val password = binding.loginPasswordInput.text.toString()
            if (validInput(email, password)) {
                Log.d("login", "Email: $email and Password: $password")
                //login(email, password.encrypt(password))
                loginWithApi(email, password)
            }
        }

        return binding.root
    }

    private fun loginWithApi(email: String, password: String) {
        lifecycleScope.launch {
            //show login progress dialog
            Log.d("auth", "login started")
            viewModel.setLoginState(true)
            try {
                val responseMain = viewModel.loginWithApi(email, password)
                if(responseMain.isSuccessful){
                    Log.d("auth", "response success")
                    val response = responseMain.body()!!
                    Log.d("auth_success", response.user.toString())
                    saveToSharedPreference(response.user)
                    viewModel.setLoginState(false)
                    findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                    activity?.finish()
                }
                else {
                    viewModel.setLoginState(false)
                    showAlert("Failed", responseMain.message())
                    Log.d("auth", "response failed")
                    Log.d("auth", "code: ${responseMain.code()}")
                    Log.d("auth", "code: ${responseMain.message()}")
                }
            }catch (e: Exception) {
                viewModel.setLoginState(false)
                Log.d("auth", "exception")
                Toast.makeText(
                    requireContext(),
                    "Login Failed. ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
                println(e.message)
            }

        }
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
                    if (it.email == email && it.password == password){
                        hasAccount = true
                        //saveToSharedPreference(it.id)
                        findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                        activity?.finish()
                        return@forEach
                    }
                }

                if(!hasAccount)
                    showAlert("Incorrect Info", "Incorrect email or password. Try Again!!")
            } else
                showAlert("Incorrect Info", "Incorrect email or password. Try Again!!")
        })
    }

    private fun saveToSharedPreference(user: User) {

        val userState = UserState(true, user)
        val gson = Gson()
        val value = gson.toJson(userState)
        Log.d("userState", value)
        val sharedPref = activity?.getSharedPreferences(USER_AUTHENTICATION_KEY, Context
            .MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString(USER_AUTHENTICATION_STATE_KEY, value)
            apply()
        }
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

    private fun setLoader(){
        val alert = AlertDialog.Builder(requireActivity()).setView(R.layout
            .progress_layout).create()
        alert.setCanceledOnTouchOutside(false)

        viewModel.loginState.observe(viewLifecycleOwner, {
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