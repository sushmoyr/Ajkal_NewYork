package com.sushmoyr.ajkalnewyork.activities.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.ActivityAuthBinding
import com.sushmoyr.ajkalnewyork.utils.Constants.AUTH_PAGE_LOGIN
import com.sushmoyr.ajkalnewyork.utils.Constants.AUTH_PAGE_REGISTER
import com.sushmoyr.ajkalnewyork.utils.Constants.AUTH_PAGE_SELECTOR_KEY

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val authPage = intent.getStringExtra(AUTH_PAGE_SELECTOR_KEY)
        navHostFragment = (supportFragmentManager.findFragmentById(R.id.auth_nav_host) as NavHostFragment)
        navController = navHostFragment.navController
        val navGraph = navController.graph
        when(authPage){
            AUTH_PAGE_LOGIN -> navGraph.startDestination = R.id.loginFragment
            AUTH_PAGE_REGISTER -> navGraph.startDestination = R.id.registrationFragment
        }
        navController.graph = navGraph
    }
}