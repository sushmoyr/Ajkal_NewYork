package com.sushmoyr.ajkalnewyork.activities.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.snackbar.Snackbar
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.ArchiveActivity
import com.sushmoyr.ajkalnewyork.activities.viewmodels.DrawerViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModelFactory
import com.sushmoyr.ajkalnewyork.databinding.ActivityMainBinding
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource
import com.sushmoyr.ajkalnewyork.utils.hasNetwork

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
        setUpDrawerRvItems()

        drawerRvAdapter.itemClickListener = {data, type ->
            Log.d("selected", data)
            drawerViewModel.selectedCategoryFilter(data)
            binding.rootDrawerLayout.closeDrawers()
        }

        binding.showGalleryButton.setOnClickListener {
            navController.navigate(R.id.action_global_galleryFragment)
            binding.rootDrawerLayout.closeDrawers()
        }

        binding.archiveButton.setOnClickListener {
            navController.navigate(R.id.action_global_archiveFragment)
            binding.rootDrawerLayout.closeDrawers()
        }

        binding.subscribeBtn.setOnClickListener {
            //TODO: subscribe
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id==R.id.galleryFragment)
                binding.mainBottomNav.visibility = View.GONE
            else
                binding.mainBottomNav.visibility = View.VISIBLE
        }

        viewModel.errorListener = {e ->
            if(e!=null){
                Log.d("exception", "Listened error")
                if(!hasNetwork(this)){
                    viewSnackBar("No internet connection. Check your connection and restart the " +
                            "app")
                }
                else{
                    viewSnackBar(resources.getString(R.string.server_error))
                }
                Log.d("exception","Has network = ${hasNetwork(this)}")
            }
        }
    }

    private fun viewSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_INDEFINITE,
        )
        snackBar.show()
    }

    private fun setUpBottomNavigation(){
        val bottomNavigationView = binding.mainBottomNav
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemReselectedListener {
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
                val categories = response.body()!!.toMutableList()
                val firstItem = Category("default", resources.getString(R.string.defaultCategoryName))
                categories.add(0, firstItem)


                drawerViewModel.setCategoryList(categories)
                drawerViewModel.selectedCategoryFilter(categories.first().categoryName)
            }
        })
    }

    private fun setUpDrawerRvItems(){
        viewModel.prepareDrawerItems()
        viewModel.drawerItemList.observe(this, { items->
            drawerRvAdapter.setData(items)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}