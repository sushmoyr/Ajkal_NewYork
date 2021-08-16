package com.sushmoyr.ajkalnewyork.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Interpolator
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.sushmoyr.ajkalnewyork.models.UserState
import java.security.MessageDigest

fun View.blink(
    times: Int = Animation.INFINITE,
    duration: Long = 500L,
    offset: Long = 20L,
    minAlpha: Float = 0.0f,
    maxAlpha: Float = 1.0f,
    repeatMode: Int = Animation.REVERSE,
) {
    startAnimation(AlphaAnimation(minAlpha, maxAlpha).also {
        it.duration = duration
        it.startOffset = offset
        it.repeatMode = repeatMode
        it.repeatCount = times
        it.interpolator = AccelerateDecelerateInterpolator()
    })
}

fun String.encrypt(plainText: String): String {
    val algorithmType = "SHA-256"
    val bytes = MessageDigest.getInstance(algorithmType).digest(plainText.toByteArray())
    return toHex(bytes)
}

private fun toHex(byteArray: ByteArray): String
{
    return byteArray.joinToString("") { "%02x".format(it) }
}

fun <T> LiveData<T>.observeOnce(
    lifecycleOwner: LifecycleOwner,
    observer: Observer<T>
) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observer.onChanged(t)
        }

    })
}

fun getUserState(activity: FragmentActivity?): UserState {
    val sharedPref =
        activity?.getSharedPreferences(Constants.USER_AUTHENTICATION_KEY, Context.MODE_PRIVATE) ?:
        return UserState(false, null)
    val value = sharedPref.getString(Constants.USER_AUTHENTICATION_STATE_KEY, "")
    if(value.isNullOrEmpty() || value.isNullOrBlank())
        return UserState(false, null)
    val gson = Gson()
    val data = gson.fromJson(value, UserState::class.java)
    Log.d("userState", "retrieved ${data.toString()}")
    return data
}

fun hasNetwork(context: Context): Boolean {
    var isConnected: Boolean = false // Initial Value
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}