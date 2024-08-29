package com.example.cineai.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cineai.R
import com.example.cineai.databinding.ActivityMainBinding
import com.example.cineai.ui.classes.MovieCategory
import com.example.cineai.ui.classes.navigateToActivity
import com.example.cineai.ui.classes.openFragment
import com.example.cineai.ui.fragment.AiRecommendationFragment
import com.example.cineai.ui.fragment.BaseMovieFragment
import com.example.cineai.ui.fragment.MovieFragment
import com.example.cineai.ui.fragment.SettingsFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (firebaseAuth.currentUser == null) {
            this.navigateToActivity(LoginActivity::class.java)
        }
        setNavigationBar()
    }

    private fun setNavigationBar() {
        binding.navigationBarView.setOnItemSelectedListener {
            val containerId = R.id.fragmentContainerView
            when (it.itemId) {
                R.id.menu_item_home -> {
                    openFragment(MovieFragment(), containerId)
                    true
                }

                R.id.menu_item_favorite -> {
                    openFragment(BaseMovieFragment.newInstance(MovieCategory.FAVORITE), containerId)
                    true
                }

                R.id.menu_item_ai -> {
                    openFragment(AiRecommendationFragment(), containerId)
                    true
                }

                R.id.settings -> {
                    openFragment(SettingsFragment(), containerId)
                    true
                }

                else -> false
            }
        }
    }
}