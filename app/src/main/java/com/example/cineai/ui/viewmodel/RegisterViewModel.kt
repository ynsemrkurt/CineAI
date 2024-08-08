package com.example.cineai.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cineai.R
import com.example.cineai.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _registrationStatus = MutableLiveData<String>()
    val registrationStatus: LiveData<String> get() = _registrationStatus

    fun registerUser(username: String, email: String, password: String, context: Context) {
        if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            validateInputs(username, email, password, context)
        } else {
            _registrationStatus.value = context.getString(R.string.invalid_input)
        }
    }

    private fun validateInputs(
        username: String,
        email: String,
        password: String,
        context: Context
    ) {
        when {
            username.length < 3 -> {
                _registrationStatus.value = context.getString(R.string.username_characters_error)
            }

            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _registrationStatus.value = context.getString(R.string.invalid_email_error)
            }

            password.length < 6 -> {
                _registrationStatus.value = context.getString(R.string.password_characters_error)
            }

            else -> checkUsernameAvailability(username, email, password, context)
        }
    }

    private fun checkUsernameAvailability(
        username: String,
        email: String,
        password: String,
        context: Context
    ) {
        firestore.collection("users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    _registrationStatus.value = context.getString(R.string.username_already_error)
                } else {
                    createUser(email, password, username, context)
                }
            }
            .addOnFailureListener { exception ->
                _registrationStatus.value =
                    context.getString(R.string.error_checking_username, exception.message)
            }
    }

    private fun createUser(email: String, password: String, username: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    saveUserToFirestore(username, userId, context)
                } else {
                    _registrationStatus.value =
                        context.getString(R.string.registration_error, task.exception?.message)
                }
            }
    }

    private fun saveUserToFirestore(username: String, userId: String?, context: Context) {
        val user = User(username, userId)
        userId?.let {
            firestore.collection("users").document(it).set(user)
                .addOnSuccessListener {
                    _registrationStatus.value = context.getString(R.string.registration_successful)
                }
                .addOnFailureListener { e ->
                    _registrationStatus.value =
                        context.getString(R.string.error_saving_user_data, e.message)
                }
        }
    }
}