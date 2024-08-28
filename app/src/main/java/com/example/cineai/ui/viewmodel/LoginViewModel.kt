package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cineai.R
import com.example.cineai.ui.classes.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _loginStatus = MutableLiveData<Int>()
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
            !email.isValidEmail() -> _loginStatus.value = R.string.invalid_email_error
            password.length < 6 -> _loginStatus.value = R.string.password_characters_error
            else -> performLogin(email, password)
        }
    }

    private fun performLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkProfile()
                } else {
                    _loginStatus.value = R.string.login_error
                }
            }
    }

    private fun checkProfile() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("profile")
            .document("profile_info")
            .get()
            .addOnSuccessListener { document ->
                _loginStatus.value = if (document.exists()) {
                    R.string.login_successful
                } else {
                    R.string.profile_missing
                }
            }
            .addOnFailureListener {
                _loginStatus.value = R.string.error_checking_profile
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