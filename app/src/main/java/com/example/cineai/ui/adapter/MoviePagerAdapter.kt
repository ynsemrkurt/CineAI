package com.example.cineai.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineai.ui.classes.MovieCategory
import com.example.cineai.ui.fragment.BaseMovieFragment

class MoviePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BaseMovieFragment.newInstance(MovieCategory.POPULAR)
            1 -> BaseMovieFragment.newInstance(MovieCategory.TOP_RATED)
            2 -> BaseMovieFragment.newInstance(MovieCategory.NOW_PLAYING)
            3 -> BaseMovieFragment.newInstance(MovieCategory.UPCOMING)
            else -> BaseMovieFragment.newInstance(MovieCategory.POPULAR)
        }
    }
}