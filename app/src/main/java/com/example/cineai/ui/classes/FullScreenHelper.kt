package com.example.cineai.ui.classes

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.cineai.R

object FullScreenHelper {

    fun enterFullScreen(activity: Activity, fullscreenView: View) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        val fullScreenLayout = activity.findViewById<FrameLayout>(R.id.fullScreenLayout)
        val viewPager2 = activity.findViewById<ViewPager2>(R.id.viewPagerMovie)
        viewPager2.visibility = View.GONE
        fullScreenLayout.visibility = View.VISIBLE
        fullScreenLayout.addView(fullscreenView)
    }

    fun exitFullScreen(activity: Activity) {
        val fullScreenLayout = activity.findViewById<FrameLayout>(R.id.fullScreenLayout)
        val viewPager2 = activity.findViewById<ViewPager2>(R.id.viewPagerMovie)
        viewPager2.visibility = View.VISIBLE
        fullScreenLayout.visibility = View.GONE
        fullScreenLayout.removeAllViews()
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}