package com.sushmoyr.ajkalnewyork.activities.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.DrawerViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModelFactory
import com.sushmoyr.ajkalnewyork.databinding.ActivityMainBinding
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: MainActivityViewModel
    private val drawerViewModel: DrawerViewModel by viewModels()
    private val drawerRvAdapter: DrawerMenuAdapter by lazy {
        DrawerMenuAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = RemoteDataSource()
        val factory = MainActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)
        //drawerViewModel = ViewModelProviders.of(this).get(DrawerViewModel::class.java)

        drawerViewModel.data = "set data from activity"


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

        drawerRvAdapter.itemClickListener = {data, type ->
            drawerViewModel.selectedCategoryFilter(data)
            binding.rootDrawerLayout.closeDrawers()
        }

        binding.showGalleryButton.setOnClickListener {
            navController.navigate(R.id.action_global_galleryFragment)
            binding.rootDrawerLayout.closeDrawers()
        }


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id==R.id.galleryFragment)
                binding.mainBottomNav.visibility = View.GONE
            else
                binding.mainBottomNav.visibility = View.VISIBLE
        }
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
            if(response.isSuccessful && response.body()!=null){
                drawerRvAdapter.setData(response.body()!!)
                drawerViewModel.setCategoryList(response.body()!!)
                drawerViewModel.selectedCategoryFilter(response.body()!!.first().categoryName)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}