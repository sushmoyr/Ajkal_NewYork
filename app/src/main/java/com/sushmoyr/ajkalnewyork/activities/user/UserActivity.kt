package com.sushmoyr.ajkalnewyork.activities.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.main.MainActivity
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.ActivityUserBinding
import com.sushmoyr.ajkalnewyork.utils.Constants
import com.sushmoyr.ajkalnewyork.utils.getUserState

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val userViewModel: MainUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userState = getUserState(this)
        if(!userState.isLoggedIn || userState.user==null)
            logout()

        userViewModel.setCurrentUser(userState.user!!)

        setUpNavigation()

        binding.logoutButton.setOnClickListener {
            logout()
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