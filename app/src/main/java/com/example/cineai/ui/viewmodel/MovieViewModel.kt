package com.example.cineai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cineai.data.model.Movie
import com.example.cineai.data.paging.MoviePagingSource
import kotlinx.coroutines.flow.Flow

class MovieViewModel : ViewModel() {

    fun getMovies(category: String): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(category) }).flow.cachedIn(viewModelScope)
    }
}