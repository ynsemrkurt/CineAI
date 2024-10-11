package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signOut(): LiveData<Boolean> {
        val signOutLiveData = MutableLiveData<Boolean>()
        firebaseAuth.signOut()
        signOutLiveData.value = true
        return signOutLiveData
    }

    fun deleteAccount(): LiveData<Result<Unit>> {
        val deleteAccountLiveData = MutableLiveData<Result<Unit>>()

        firebaseAuth.currentUser?.delete()?.let { task ->
            task.addOnSuccessListener {
                deleteAccountLiveData.value = Result.success(Unit)
            }
            task.addOnFailureListener { exception ->
                deleteAccountLiveData.value = Result.failure(exception)
            }
        }

        return deleteAccountLiveData
    }
}
