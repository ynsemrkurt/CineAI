package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cineai.R
import com.example.cineai.data.model.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileSetupViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _profileSetupStatus = MutableLiveData<Int>()
    val profileSetupStatus: LiveData<Int> get() = _profileSetupStatus

    fun saveProfile(profile: Profile) {
        if (isProfileValid(profile)) {
            saveProfileToFirestore(userId, profile)
        } else {
            _profileSetupStatus.value = R.string.all_fields_required
        }
    }

    private fun isProfileValid(profile: Profile): Boolean {
        return profile.stress.isNotEmpty() &&
                profile.problemSolving.isNotEmpty() &&
                profile.decisionMaking.isNotEmpty() &&
                profile.teamwork.isNotEmpty() &&
                profile.movieGenres.isNotEmpty() &&
                profile.music.isNotEmpty() &&
                profile.hobbies.isNotEmpty() &&
                profile.travel.isNotEmpty()
    }

    private fun saveProfileToFirestore(userId: String, profile: Profile) {
        val profileData = hashMapOf(
            "stress" to profile.stress,
            "problemSolving" to profile.problemSolving,
            "decisionMaking" to profile.decisionMaking,
            "teamwork" to profile.teamwork,
            "movieGenres" to profile.movieGenres,
            "music" to profile.music,
            "hobbies" to profile.hobbies,
            "travel" to profile.travel
        )
        firestore.collection("users").document(userId).collection("profile")
            .document("profile_info")
            .set(profileData)
            .addOnSuccessListener {
                _profileSetupStatus.value = R.string.profile_setup_successful
            }
            .addOnFailureListener {
                _profileSetupStatus.value = R.string.error_saving_profile_data
            }
    }
}