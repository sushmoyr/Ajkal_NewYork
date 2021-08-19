package com.sushmoyr.ajkalnewyork.models

import com.sushmoyr.ajkalnewyork.models.utility.User

data class UserState(
    val isLoggedIn: Boolean,
    val user: User?
)
