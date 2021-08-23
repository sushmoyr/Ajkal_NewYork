package com.sushmoyr.ajkalnewyork.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sushmoyr.ajkalnewyork.activities.auth.AuthActivity
import com.sushmoyr.ajkalnewyork.databinding.ActivityInfoBinding
import com.sushmoyr.ajkalnewyork.utils.Constants.AUTH_PAGE_SELECTOR_KEY
import com.sushmoyr.ajkalnewyork.utils.Constants.AUTH_PAGE_LOGIN
import com.sushmoyr.ajkalnewyork.utils.Constants.AUTH_PAGE_REGISTER
import com.sushmoyr.ajkalnewyork.utils.getUserState

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userState = getUserState(this)

        if(userState.isLoggedIn){
            binding.infoLoginButton.visibility = View.INVISIBLE
            binding.infoRegButton.visibility = View.INVISIBLE
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.infoLoginButton.setOnClickListener {
            startActivity(
                Intent(this, AuthActivity::class.java)
                    .putExtra(AUTH_PAGE_SELECTOR_KEY, AUTH_PAGE_LOGIN)
            )
        }
        binding.infoRegButton.setOnClickListener {
            startActivity(
                Intent(this, AuthActivity::class.java)
                    .putExtra(AUTH_PAGE_SELECTOR_KEY, AUTH_PAGE_REGISTER)
            )
        }
    }
}