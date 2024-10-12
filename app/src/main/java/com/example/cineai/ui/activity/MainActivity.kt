package com.example.cineai.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cineai.R
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.databinding.ActivityMainBinding
import com.example.cineai.ui.classes.MovieCategory
import com.example.cineai.ui.classes.isInternetAvailable
import com.example.cineai.ui.classes.navigateToActivity
import com.example.cineai.ui.classes.openFragment
import com.example.cineai.ui.classes.restartCurrentActivity
import com.example.cineai.ui.classes.showNoInternetDialog
import com.example.cineai.ui.fragment.AiRecommendationFragment
import com.example.cineai.ui.fragment.BaseMovieFragment
import com.example.cineai.ui.fragment.MovieFragment
import com.example.cineai.ui.fragment.SearchFragment
import com.example.cineai.ui.fragment.SettingsFragment
import com.example.cineai.ui.viewmodel.LoginViewModel
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternetConnection()
        checkUserStatus()
        initializeAds()
        setNavigationBar()
        setLanguage()
    }

    private fun initializeAds() {
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@MainActivity)
        }
    }

    private fun checkUserStatus() {
        if (firebaseAuth.currentUser == null) {
            this.navigateToActivity(LoginActivity::class.java)
        } else {
            loginViewModel.checkProfile()
            observeLoginStatus()
        }
    }

    private fun setLanguage() {
        RetrofitClient.setLanguage(resources.configuration.locales.get(0).language)
    }

    private fun checkInternetConnection() {
        if (!isInternetAvailable()) {
            showNoInternetDialog { restartCurrentActivity() }
        }
    }

    private fun observeLoginStatus() {
        loginViewModel.loginStatus.observe(this) { status ->
            when (status) {
                R.string.profile_missing -> {
                    firebaseAuth.signOut()
                    this.navigateToActivity(LoginActivity::class.java)
                }
            }
        }
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

                R.id.menu_item_search -> {
                    openFragment(SearchFragment(), containerId)
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