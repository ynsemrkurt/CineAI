package com.example.cineai.ui.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cineai.R
import com.example.cineai.ui.classes.isValidEmail
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginStatus = MutableLiveData<@receiver:StringRes Int>()
    val loginStatus: LiveData<Int> get() = _loginStatus

    fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            validateInputs(email, password)
        } else {
            _loginStatus.value = R.string.invalid_input
        }
    }

    private fun validateInputs(email: String, password: String) {
        when {
            !email.isValidEmail() -> {
                _loginStatus.value = R.string.invalid_email_error
            }

            password.length < 6 -> {
                _loginStatus.value = R.string.password_characters_error
            }

            else -> performLogin(email, password)
        }
    }

    private fun performLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginStatus.value = R.string.login_successful
                } else {
                    _loginStatus.value = R.string.login_error
                }
            }
    }

    fun sendPasswordResetEmail(email: String, onDismiss: () -> Unit) {
        if (!email.isValidEmail()) {
            _loginStatus.value = R.string.invalid_email_error
        } else {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginStatus.value = R.string.password_reset_sent_successfully
                        onDismiss()
                    } else {
                        _loginStatus.value = R.string.error_send
                    }
                }
        }
    }
}
