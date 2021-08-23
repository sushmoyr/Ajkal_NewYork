package com.sushmoyr.ajkalnewyork.activities.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.main.MainActivity
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.ActivityUserBinding
import com.sushmoyr.ajkalnewyork.fragments.user.UserStateViewModel
import com.sushmoyr.ajkalnewyork.models.UserState
import com.sushmoyr.ajkalnewyork.utils.Constants
import com.sushmoyr.ajkalnewyork.utils.Constants.USER_AUTHENTICATION_STATE_KEY
import com.sushmoyr.ajkalnewyork.utils.getUserState

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val userViewModel: MainUserViewModel by viewModels()
    private val userStateViewModel: UserStateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userState = getUserState(this)
        if(!userState.isLoggedIn || userState.user==null)
            logout()
        else{
            userStateViewModel.updateUserStateFromNetwork(userState.user.id.toString())
        }

        userStateViewModel.userStateUpdater.observe(this, { userState ->
            saveToSharedPreference(userState)
            userViewModel.setCurrentUser(userState.user!!)
        })

        userViewModel.setCurrentUser(userState.user!!)

        setUpNavigation()

        binding.logoutButton.setOnClickListener {
            logout()
        }
        binding.homeButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveToSharedPreference(userState: UserState?) {
        val gson = Gson()
        val value = gson.toJson(userState)
        Log.d("userState", value)
        val sharedPref = this.getSharedPreferences(
            Constants.USER_AUTHENTICATION_KEY, Context
                .MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString(USER_AUTHENTICATION_STATE_KEY, value)
            apply()
        }
    }

    private fun logout() {
        val sharedPref = this.getSharedPreferences(
            Constants.USER_AUTHENTICATION_KEY, Context
                .MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setUpNavigation(){
        navHostFragment = (supportFragmentManager.findFragmentById(R.id.user_fragment_container) as NavHostFragment)
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.userRootDrawer)
        binding.userNavigationView.setupWithNavController(navController)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}