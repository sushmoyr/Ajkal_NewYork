package com.sushmoyr.ajkalnewyork

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sushmoyr.ajkalnewyork.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = (supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment)
        navController = navHostFragment.navController
        val toolbar = binding.toolbar
        drawerLayout = binding.rootDrawerLayout

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation(){
        val bottomNavigationView = binding.mainBottomNav
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemReselectedListener {
            //disabled reselect
        }

        val bottomNavDestinations = listOf(R.id.homeFragment, R.id.trendingFragment, R.id
            .videosFragment, R.id.mapFragment)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(bottomNavDestinations.contains(destination.id)){
                supportActionBar!!.show()
                binding.appbar.visibility = View.VISIBLE
            }
            else{
                supportActionBar!!.hide()
                binding.appbar.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}