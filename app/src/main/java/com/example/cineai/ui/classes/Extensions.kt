package com.example.cineai.ui.classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cineai.BuildConfig
import com.example.cineai.R

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun <T : Parcelable> Fragment.getParcelable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getParcelable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        arguments?.getParcelable(key)
    }
}

fun <T : Parcelable> Fragment.putParcelable(key: String, value: T) {
    arguments = Bundle().apply {
        putParcelable(key, value)
    }
}

fun String.isValidEmail(): Boolean {
    return this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Fragment.openFragment(containerViewId: Int, fragment: Fragment, args: Bundle? = null) {
    fragment.arguments = args
    parentFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .replace(containerViewId, fragment)
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.openFragment(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction()
        .replace(containerId, fragment)
        .addToBackStack(null)
        .commit()
}


fun ImageView.loadImage(url: String, placeholderResId: Int = R.drawable.image_32) {
    Glide.with(this)
        .load(url)
        .placeholder(placeholderResId)
        .into(this)
}

fun Window.hideSystemUI() {
    setBackgroundDrawableResource(android.R.color.transparent)
    setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setDecorFitsSystemWindows(false)
        insetsController?.let {
            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}

fun Context.navigateToActivity(activityClass: Class<*>) {
    startActivity(Intent(this, activityClass))
    if (this is Activity) finish()
}

fun String?.toImageUrl(size: ImageSize): String {
    return BuildConfig.IMAGE_BASE_URL + size.value + this
}