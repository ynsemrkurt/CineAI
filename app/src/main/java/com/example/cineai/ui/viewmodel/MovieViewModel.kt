package com.example.cineai.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineai.data.model.Movie
import com.example.cineai.data.model.MovieResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchMovies(call: Call<MovieResponse>) {
        viewModelScope.launch {
            call.enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        _movies.value = response.body()?.results
                    } else {
                        _errorMessage.value = "Response not successful"
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    _errorMessage.value = t.message
                }
            })
        }
    }
}