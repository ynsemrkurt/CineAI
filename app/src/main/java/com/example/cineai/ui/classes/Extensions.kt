package com.example.cineai.ui.classes

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.fragment.app.Fragment

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
