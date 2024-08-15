package com.example.cineai.ui.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cineai.R
import com.example.cineai.data.model.User
import com.example.cineai.ui.classes.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _registrationStatus = MutableLiveData<@receiver:StringRes Int>()
    val registrationStatus: LiveData<Int> get() = _registrationStatus

    fun registerUser(username: String, email: String, password: String) {
        if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            validateInputs(username, email, password)
        } else {
            _registrationStatus.value = R.string.invalid_input
        }
    }

    private fun validateInputs(
        username: String,
        email: String,
        password: String
    ) {
        when {
            username.length < 3 -> {
                _registrationStatus.value = R.string.username_characters_error
            }

            !email.isValidEmail() -> {
                _registrationStatus.value = R.string.invalid_email_error
            }

            password.length < 6 -> {
                _registrationStatus.value = R.string.password_characters_error
            }

            else -> checkUsernameAvailability(username, email, password)
        }
    }

    private fun checkUsernameAvailability(
        username: String,
        email: String,
        password: String
    ) {
        firestore.collection("users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    _registrationStatus.value = R.string.username_already_error
                } else {
                    createUser(email, password, username)
                }
            }
            .addOnFailureListener {
                _registrationStatus.value = R.string.error_checking_username
            }
    }

    private fun createUser(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    saveUserToFirestore(username, userId)
                } else {
                    _registrationStatus.value = R.string.registration_error
                }
            }
    }

    private fun saveUserToFirestore(username: String, userId: String?) {
        val user = User(username, userId)
        userId?.let {
            firestore.collection("users").document(it).set(user)
                .addOnSuccessListener {
                    _registrationStatus.value = R.string.registration_successful
                }
                .addOnFailureListener {
                    _registrationStatus.value = R.string.error_saving_user_data
                }
        }
    }
}