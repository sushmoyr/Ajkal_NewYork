package com.sushmoyr.ajkalnewyork.utils

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Interpolator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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