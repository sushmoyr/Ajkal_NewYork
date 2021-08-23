package com.sushmoyr.ajkalnewyork.models.utility.transactionhistory


import com.google.gson.annotations.SerializedName
import com.sushmoyr.ajkalnewyork.models.utility.transactionhistory.Advertisements

data class TransactionHistory(
    @SerializedName("advertisements")
    val advertisements:List< Advertisements> = listOf()
)