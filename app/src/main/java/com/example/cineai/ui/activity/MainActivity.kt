package com.example.cineai.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cineai.R
import com.example.cineai.databinding.ActivityMainBinding
import com.example.cineai.ui.classes.MovieCategory
import com.example.cineai.ui.fragment.BaseMovieFragment
import com.example.cineai.ui.fragment.MovieFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigationBar()
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