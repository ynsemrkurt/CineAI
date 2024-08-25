package com.example.cineai.ui.classes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cineai.R

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
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

fun Fragment.openFragment(containerViewId: Int, fragment: Fragment) {
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

fun Activity.playYouTubeVideoFullScreen(videoId: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
