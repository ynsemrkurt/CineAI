package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cineai.data.model.CharacterResponse
import com.example.cineai.data.model.Movie
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.data.paging.MoviePagingSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MovieViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _movieDetails = MutableLiveData<Movie>()
    val movieDetails: LiveData<Movie> get() = _movieDetails

    private val _video = MutableLiveData<String>()
    val video: LiveData<String> get() = _video

    private val _character = MutableLiveData<CharacterResponse>()
    val character: LiveData<CharacterResponse> get() = _character

    private val _favoriteMovieIds = MutableLiveData<List<String>>()
    val favoriteMovieIds: LiveData<List<String>> get() = _favoriteMovieIds

    fun addMovieToFavorites(movieId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("favorites").document(movieId)
            .set(mapOf("id" to movieId))
    }

    fun removeMovieFromFavorites(movieId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("favorites").document(movieId)
            .delete()
    }

    fun isMovieFavorite(movieId: String, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("favorites").document(movieId)
            .get().addOnSuccessListener { document ->
                callback(document.exists())
            }.addOnFailureListener {
                callback(false)
            }
    }

    fun loadFavoriteMovieIds() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val snapshot =
                    firestore.collection("users").document(userId).collection("favorites").get()
                        .await()

                val ids = snapshot.documents.mapNotNull { it.getString("id") }
                _favoriteMovieIds.postValue(ids)
            } catch (e: Exception) {
                _favoriteMovieIds.postValue(emptyList())
            }
        }
    }


    fun getMovies(category: String, favList: List<String>? = null): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(category, favList) }).flow.cachedIn(
            viewModelScope
        )
    }

    fun fetchMovieDetails(movieId: String) {
        viewModelScope.launch {
            try {
                val movie = RetrofitClient.api.getDetailsMovies(movieId)
                _movieDetails.postValue(movie)
                fetchVideo(movieId)
                fetchCharacter(movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchVideo(movieId: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.searchVideo(movieId).results.find { it.type == "Trailer" }?.let {
                    _video.postValue(it.key)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchCharacter(movieId: String) {
        viewModelScope.launch {
            try {
                val character = RetrofitClient.api.searchCharacter(movieId)
                _character.postValue(character)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}