package com.example.cineai.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineai.data.model.Movie
import com.example.cineai.data.network.RetrofitClient

class MoviePagingSource(
    private val category: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1
        return try {
            val response = when (category) {
                "popular" -> RetrofitClient.api.getPopularMovies(position)
                "top_rated" -> RetrofitClient.api.getTopRatedMovies(position)
                "now_playing" -> RetrofitClient.api.getNowPlayingMovies(position)
                "upcoming" -> RetrofitClient.api.getUpcomingMovies(position)
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