package com.example.cineai.ui.classes

import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class LoadingAnim {

    fun showLoadingAnimation(animationView: LottieAnimationView, textView: TextView) {
        animationView.visibility = View.VISIBLE
        textView.visibility = View.GONE
    }

    fun hideLoadingAnimation(animationView: LottieAnimationView, textView: TextView) {
        animationView.visibility = View.GONE
        textView.visibility = View.VISIBLE
    }
}