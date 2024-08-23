package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cineai.BuildConfig
import com.example.cineai.R
import com.example.cineai.data.model.CharacterResponse
import com.example.cineai.data.model.Movie
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.data.paging.MoviePagingSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.awaitResponse

class MovieViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _movieDetails = MutableLiveData<Movie>()
    val movieDetails: LiveData<Movie> get() = _movieDetails

    private val _character = MutableLiveData<CharacterResponse>()
    val character: LiveData<CharacterResponse> get() = _character

    private val _favoriteMovieIds = MutableLiveData<List<String>>()
    val favoriteMovieIds: LiveData<List<String>> get() = _favoriteMovieIds

    private val _thumbnail = MutableLiveData<String>()
    val thumbnail: LiveData<String> get() = _thumbnail

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> get() = _error

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
                _error.postValue(R.string.error_loading_favorite)
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
                _error.postValue(R.string.error_fetching_details)
            }
        }
    }

    private fun fetchVideo(movieId: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.searchVideo(movieId).results.find { it.type == "Trailer" }?.let {
                    fetchThumbnail(it.key)
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

    private fun fetchThumbnail(videoID: String) {
        viewModelScope.launch {
            try {
                val thumbnail = RetrofitClient.ytApi.getVideoDetails("snippet", videoID, BuildConfig.YT_API_KEY)
                _thumbnail.postValue(thumbnail.awaitResponse().body()?.items?.get(0)?.snippet?.thumbnails?.maxres?.url)
            } catch (e: Exception) {
                _error.postValue(R.string.error_fetching_thumbnail)
            }
        }
    }
}