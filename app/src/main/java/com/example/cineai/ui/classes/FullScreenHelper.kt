package com.example.cineai.ui.classes

import android.app.Activity
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.view.View
import android.view.Window
import androidx.viewpager2.widget.ViewPager2
import com.example.cineai.R

object FullScreenHelper {

    private var dialog: Dialog? = null

    fun enterFullScreen(activity: Activity, fullscreenView: View) {
        val viewPager2 = activity.findViewById<ViewPager2>(R.id.viewPagerMovie)
        viewPager2.visibility = View.GONE

        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        dialog = Dialog(activity).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(fullscreenView)
            window?.hideSystemUI(activity)

            setOnDismissListener {
                exitFullScreen(activity)
            }
        }

        dialog?.show()
    }

    fun exitFullScreen(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        dialog?.dismiss()

        val viewPager2 = activity.findViewById<ViewPager2>(R.id.viewPagerMovie)
        viewPager2.visibility = View.VISIBLE
    }
}