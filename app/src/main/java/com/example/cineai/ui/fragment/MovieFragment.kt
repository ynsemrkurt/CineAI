package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cineai.R
import com.example.cineai.databinding.FragmentMovieBinding
import com.example.cineai.ui.adapter.MoviePagerAdapter
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
}