package com.sushmoyr.ajkalnewyork.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val address: String = "N/A",
    val phoneNo: String = "N/A",
    val profilePhoto : Bitmap
)
