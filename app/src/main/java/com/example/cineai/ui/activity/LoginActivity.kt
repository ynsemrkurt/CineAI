package com.example.cineai.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cineai.R
import com.example.cineai.ui.classes.isInternetAvailable
import com.example.cineai.ui.classes.restartCurrentActivity
import com.example.cineai.ui.classes.showNoInternetDialog
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (!isInternetAvailable()) {
            showNoInternetDialog { restartCurrentActivity() }
        }

        if (auth.currentUser != null) {
            auth.signOut()
        }
    }
}