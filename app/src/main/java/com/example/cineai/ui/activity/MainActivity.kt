package com.example.cineai.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cineai.R
import com.example.cineai.databinding.ActivityMainBinding
import com.example.cineai.ui.adapter.MoviePagerAdapter
import com.example.cineai.ui.classes.MovieCategory
import com.example.cineai.ui.fragment.BaseMovieFragment
import com.example.cineai.ui.fragment.MovieFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigationBar()
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

    private fun setNavigationBar() {
        binding.navigationBarView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_home -> {
                    openFragment(MovieFragment())
                    true
                }

                R.id.menu_item_favorite -> {
                    openFragment(BaseMovieFragment.newInstance(MovieCategory.FAVORITE))
                    true
                }

                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }
}