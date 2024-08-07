package com.example.cineai.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineai.data.model.Movie
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.ui.classes.MovieCategory

class MoviePagingSource(
    private val category: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1
        return try {
            val response = when (category) {
                MovieCategory.POPULAR.value -> RetrofitClient.api.getPopularMovies(position)
                MovieCategory.TOP_RATED.value -> RetrofitClient.api.getTopRatedMovies(position)
                MovieCategory.NOW_PLAYING.value -> RetrofitClient.api.getNowPlayingMovies(position)
                MovieCategory.UPCOMING.value -> RetrofitClient.api.getUpcomingMovies(position)
                else -> RetrofitClient.api.getPopularMovies(position)
            }

            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}