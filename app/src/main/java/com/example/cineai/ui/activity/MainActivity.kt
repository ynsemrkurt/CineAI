package com.example.cineai.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineai.R
import com.example.cineai.databinding.ActivityMainBinding
import com.example.cineai.ui.classes.MovieCategory
import com.example.cineai.ui.fragment.BaseMovieFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        binding.viewPagerMovie.adapter = MoviePagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPagerMovie) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.popular)
                1 -> getString(R.string.top_rated)
                2 -> getString(R.string.now_playing)
                3 -> getString(R.string.upcoming)
                else -> null
            }
        }.attach()
    }

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
}