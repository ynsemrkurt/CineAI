package com.example.cineai.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cineai.R
import com.example.cineai.databinding.ActivityMainBinding
import com.example.cineai.ui.classes.MovieCategory
import com.example.cineai.ui.fragment.AiRecommendationFragment
import com.example.cineai.ui.fragment.BaseMovieFragment
import com.example.cineai.ui.fragment.MovieFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

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

                R.id.menu_item_ai -> {
                    openFragment(AiRecommendationFragment())
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