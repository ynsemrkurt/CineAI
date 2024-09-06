package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cineai.R
import com.example.cineai.data.model.CharacterResponse
import com.example.cineai.data.model.Movie
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.data.paging.MoviePagingSource
import com.example.cineai.ui.classes.FirestoreConstants.COLLECTION_FAVORITES
import com.example.cineai.ui.classes.FirestoreConstants.COLLECTION_USERS
import com.example.cineai.ui.classes.FirestoreConstants.FIELD_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MovieViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _movieDetails = MutableLiveData<Movie>()
    val movieDetails: LiveData<Movie> get() = _movieDetails

    private val _character = MutableLiveData<CharacterResponse>()
    val character: LiveData<CharacterResponse> get() = _character

    private val _favoriteMovieIds = MutableLiveData<List<String>>()
    val favoriteMovieIds: LiveData<List<String>> get() = _favoriteMovieIds

    private val _videoId = MutableLiveData<String>()
    val videoId: LiveData<String> get() = _videoId

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> get() = _error

    private val _movieBackdrops = MutableLiveData<List<String>>()
    val movieBackdrops: LiveData<List<String>> get() = _movieBackdrops

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    fun addMovieToFavorites(movieId: String) {
        updateFavoriteMovie(movieId, isAdding = true)
    }

    fun removeMovieFromFavorites(movieId: String) {
        updateFavoriteMovie(movieId, isAdding = false)
    }

    companion object {
        private const val TRAILER = "Trailer"
    }

    private fun updateFavoriteMovie(movieId: String, isAdding: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val action = if (isAdding) {
            firestore.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_FAVORITES)
                .document(movieId)
                .set(mapOf(FIELD_ID to movieId))
        } else {
            firestore.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_FAVORITES)
                .document(movieId)
                .delete()
        }
        action.addOnFailureListener {
            _error.value = R.string.error_updating_favorite
        }
    }

    fun isMovieFavorite(movieId: String, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_FAVORITES)
            .document(movieId)
            .get()
            .addOnSuccessListener { document ->
                callback(document.exists())
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun loadFavoriteMovieIds() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot =
                    firestore.collection(COLLECTION_USERS).document(userId).collection(
                        COLLECTION_FAVORITES
                    ).get()
                        .await()
                val ids = snapshot.documents.mapNotNull { it.getString(FIELD_ID) }
                _favoriteMovieIds.postValue(ids)
            } catch (e: Exception) {
                _error.postValue(R.string.error_loading_favorite)
            }
        }
    }

    fun getMovies(category: String, favList: List<String>? = null): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(category, favList) }
        ).flow.cachedIn(viewModelScope)
    }

    fun fetchMovieDetails(movieId: String) {
        viewModelScope.launch {
            try {
                val movie = RetrofitClient.api.getDetailsMovies(movieId)
                _movieDetails.postValue(movie)
                fetchVideo(movieId)
                fetchCharacter(movieId)
                fetchMovieBackdrops(movieId)
            } catch (e: Exception) {
                _error.postValue(R.string.error_fetching_details)
            }
        }
    }

    private fun fetchVideo(movieId: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.searchVideo(movieId).results.find { it.type == TRAILER }?.let {
                    _videoId.postValue(it.key)
                }
            } catch (e: Exception) {
                _error.postValue(R.string.error_fetching_video)
            }
        }
    }

    private fun fetchCharacter(movieId: String) {
        viewModelScope.launch {
            try {
                val character = RetrofitClient.api.searchCharacter(movieId)
                _character.postValue(character)
            } catch (e: Exception) {
                _error.postValue(R.string.error_fetching_character)
            }
        }
    }

    private fun fetchMovieBackdrops(movieId: String) {
        viewModelScope.launch {
            try {
                val movieBackdrops =
                    RetrofitClient.api.getMovieBackdrops(movieId).backdrops.map { it.filePath }
                _movieBackdrops.postValue(movieBackdrops)
            } catch (e: Exception) {
                _error.postValue(R.string.error_fetching_movie_backdrops)
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                val movie = RetrofitClient.api.searchMovies(query).results
                _movies.postValue(movie)
            } catch (e: Exception) {
                _error.postValue(R.string.error_search_movies)
            }
        }
    }
}