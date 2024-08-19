package com.example.cineai.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cineai.R
import com.example.cineai.ui.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            loginViewModel.checkProfile()
            observeLoginStatus()
        }
    }

    private fun observeLoginStatus() {
        loginViewModel.loginStatus.observe(this) { status ->
            when (status) {
                R.string.login_successful -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                R.string.profile_missing -> {
                    auth.signOut()
                }
            }
        }
    }
}