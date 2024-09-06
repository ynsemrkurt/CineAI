package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cineai.R
import com.example.cineai.data.model.Profile
import com.example.cineai.ui.classes.FirestoreConstants.COLLECTION_PROFILE
import com.example.cineai.ui.classes.FirestoreConstants.COLLECTION_USERS
import com.example.cineai.ui.classes.FirestoreConstants.DOCUMENT_PROFILE_INFO
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_DECISION_MAKING
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_HOBBIES
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_MOVIE_GENRES
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_MUSIC
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_PROBLEM_SOLVING
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_STRESS
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_TEAMWORK
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_TRAVEL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileSetupViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _profileSetupStatus = MutableLiveData<Int>()
    val profileSetupStatus: LiveData<Int> get() = _profileSetupStatus

    private val _profileData = MutableLiveData<Profile>()
    val profileData: LiveData<Profile> get() = _profileData

    fun saveProfile(profile: Profile) {
        if (isProfileValid(profile)) {
            saveProfileToFirestore(profile)
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

    private fun saveProfileToFirestore(profile: Profile) {
        val profileData = hashMapOf(
            FIELD_STRESS to profile.stress,
            FIELD_PROBLEM_SOLVING to profile.problemSolving,
            FIELD_DECISION_MAKING to profile.decisionMaking,
            FIELD_TEAMWORK to profile.teamwork,
            FIELD_MOVIE_GENRES to profile.movieGenres,
            FIELD_MUSIC to profile.music,
            FIELD_HOBBIES to profile.hobbies,
            FIELD_TRAVEL to profile.travel
        )
        firestore.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_PROFILE)
            .document(DOCUMENT_PROFILE_INFO)
            .set(profileData)
            .addOnSuccessListener {
                _profileSetupStatus.value = R.string.profile_setup_successful
            }
            .addOnFailureListener {
                _profileSetupStatus.value = R.string.error_saving_profile_data
            }
    }

    fun loadProfileData() {
        firestore.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_PROFILE)
            .document(DOCUMENT_PROFILE_INFO)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profile = Profile(
                        stress = document.getString(FIELD_STRESS) ?: "",
                        problemSolving = document.getString(FIELD_PROBLEM_SOLVING) ?: "",
                        decisionMaking = document.getString(FIELD_DECISION_MAKING) ?: "",
                        teamwork = document.getString(FIELD_TEAMWORK) ?: "",
                        movieGenres = document.getString(FIELD_MOVIE_GENRES) ?: "",
                        music = document.getString(FIELD_MUSIC) ?: "",
                        hobbies = document.getString(FIELD_HOBBIES) ?: "",
                        travel = document.getString(FIELD_TRAVEL) ?: ""
                    )
                    _profileData.value = profile
                } else {
                    _profileSetupStatus.value = R.string.profile_data_not_found
                }
            }
            .addOnFailureListener {
                _profileSetupStatus.value = R.string.error_loading_profile_data
            }
    }
}