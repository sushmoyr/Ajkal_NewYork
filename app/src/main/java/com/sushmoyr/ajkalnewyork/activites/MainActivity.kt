package com.sushmoyr.ajkalnewyork.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushmoyr.ajkalnewyork.DrawerMenuAdapter
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.ActivityMainBinding
import com.sushmoyr.ajkalnewyork.repository.Repository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: MainActivityViewModel
    private val drawerRvAdapter: DrawerMenuAdapter by lazy {
        DrawerMenuAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository()
        val factory = MainActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)


        navHostFragment = (supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment)
        navController = navHostFragment.navController
        val toolbar = binding.toolbar
        drawerLayout = binding.rootDrawerLayout

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setUpBottomNavigation()
        setUpDrawerRv()
        fetchCategories()
    }

    private fun setUpBottomNavigation(){
        val bottomNavigationView = binding.mainBottomNav
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemReselectedListener {
            //disabled reselect
        }
    }

    private fun setUpDrawerRv(){
        val rv = binding.categoryListRv
        val lm = LinearLayoutManager(this)
        rv.layoutManager = lm
        rv.adapter = drawerRvAdapter
    }

    private fun fetchCategories(){
        viewModel.getAllCats()
        viewModel.allCategories.observe(this, {response->
            if(response.isSuccessful){
                drawerRvAdapter.setData(response.body()!!)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}