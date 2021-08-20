package com.sushmoyr.ajkalnewyork.models

import com.sushmoyr.ajkalnewyork.models.core.ads.Advertisement

data class AdPostResponse(
    val status: String,
    val code: Int,
    val message: String?,
    val body: Advertisement?
)
