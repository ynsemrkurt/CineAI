package com.example.cineai.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cineai.R
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> get() = _loginStatus

    fun loginUser(email: String, password: String, context: Context) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            validateInputs(email, password, context)
        } else {
            _loginStatus.value = context.getString(R.string.invalid_input)
        }
    }

    private fun validateInputs(email: String, password: String, context: Context) {
        when {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _loginStatus.value = context.getString(R.string.invalid_email_error)
            }

            password.length < 6 -> {
                _loginStatus.value = context.getString(R.string.password_characters_error)
            }

            else -> performLogin(email, password, context)
        }
    }

    private fun performLogin(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginStatus.value = context.getString(R.string.login_successful)
                } else {
                    _loginStatus.value =
                        context.getString(R.string.login_error, task.exception?.message)
                }
            }
    }

    fun sendPasswordResetEmail(email: String, context: Context) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginStatus.value = context.getString(R.string.invalid_email_error)
        } else {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginStatus.value =
                            context.getString(R.string.password_reset_sent_successfully)
                    } else {
                        _loginStatus.value =
                            context.getString(R.string.error_send, task.exception?.message)
                    }
                }
        }
    }
}
