package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineai.BuildConfig
import com.example.cineai.R
import com.example.cineai.data.model.Movie
import com.example.cineai.data.model.Profile
import com.example.cineai.data.network.RetrofitClient
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AiRecommendationViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    private val _recommendationStatus = MutableLiveData<Int>()
    val recommendationStatus: LiveData<Int> get() = _recommendationStatus

    private val _recommendations = MutableLiveData<String>()
    val recommendations: LiveData<String> get() = _recommendations

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    companion object {
        private const val AI_MODEL = "gemini-1.5-flash"
    }

    fun fetchAndRecommendMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            val profile = fetchUserProfile()
            if (profile != null) {
                getMovieRecommendations(profile)
            } else {
                _recommendationStatus.postValue(R.string.error_fetching_profile)
            }
        }
    }

    private suspend fun fetchUserProfile(): Profile? {
        return try {
            val document = userId?.let {
                firestore.collection("users").document(it)
                    .collection("profile").document("profile_info").get().await()
            }
            document?.let {
                if (it.exists()) it.toObject(Profile::class.java) else null
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getMovieRecommendations(profile: Profile) {
        val model = GenerativeModel(
            modelName = AI_MODEL,
            apiKey = BuildConfig.AI_API_KEY
        )

        val prompt = buildPromptFromProfile(profile)
        try {
            val response = model.generateContent(prompt)
            val recommendedMovies = response.text.toString().trim().split("\n")

            if (recommendedMovies.isEmpty()) {
                _recommendationStatus.postValue(R.string.no_recommendations)
            } else {
                val formattedRecommendations =
                    recommendedMovies.take(3).joinToString(separator = "\n")
                _recommendations.postValue(formattedRecommendations)
            }
        } catch (e: Exception) {
            _recommendationStatus.postValue(R.string.error_generating_recommendations)
        }
    }

    private fun buildPromptFromProfile(profile: Profile): String {
        return """
            Based on the following profile information, recommend 3 movies. Provide only movie names separated by a newline:
            Stress Management: ${profile.stress}
            Problem Solving: ${profile.problemSolving}
            Decision Making: ${profile.decisionMaking}
            Teamwork: ${profile.teamwork}
            Movie Genres: ${profile.movieGenres}
            Music: ${profile.music}
            Hobbies: ${profile.hobbies}
            Travel: ${profile.travel}
        """.trimIndent()
    }

    fun fetchMovies(movieTitles: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieList = movieTitles.mapNotNull { movieName ->
                try {
                    RetrofitClient.api.searchMovies(movieName).results.find { it.title == movieName }
                } catch (e: Exception) {
                    null
                }
            }
            _movies.postValue(movieList)
        }
    }
}