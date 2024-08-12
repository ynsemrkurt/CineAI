package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineai.R
import com.example.cineai.databinding.FragmentMovieBinding
import com.example.cineai.ui.classes.MovieCategory
import com.google.android.material.tabs.TabLayoutMediator

class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.viewPagerMovie.adapter = MoviePagerAdapter(requireActivity())
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