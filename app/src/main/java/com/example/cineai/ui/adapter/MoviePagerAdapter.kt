package com.example.cineai.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineai.ui.classes.MovieCategory.*
import com.example.cineai.ui.fragment.BaseMovieFragment

class MoviePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return BaseMovieFragment.newInstance(
            when (position) {
                0 -> POPULAR
                1 -> TOP_RATED
                2 -> NOW_PLAYING
                3 -> UPCOMING
                else -> POPULAR
            }
        )
    }
}