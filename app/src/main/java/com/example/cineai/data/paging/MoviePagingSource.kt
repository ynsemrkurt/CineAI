package com.example.cineai.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineai.data.model.Movie
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.ui.classes.MovieCategory

class MoviePagingSource(
    private val category: String,
    private val favList: List<String>? = null
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1
        return try {
            val movies: List<Movie> = when (category) {
                MovieCategory.POPULAR.value -> RetrofitClient.api.getPopularMovies(position).results
                MovieCategory.TOP_RATED.value -> RetrofitClient.api.getTopRatedMovies(position).results
                MovieCategory.NOW_PLAYING.value -> RetrofitClient.api.getNowPlayingMovies(position).results
                MovieCategory.FAVORITE.value -> favList?.drop((position - 1) * params.loadSize)
                    ?.take(params.loadSize)
                    ?.map { movieId ->
                        RetrofitClient.api.getDetailsMovies(movieId)
                    } ?: emptyList()
                MovieCategory.UPCOMING.value -> RetrofitClient.api.getUpcomingMovies(position).results
                else -> emptyList()
            }

            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movies.size < params.loadSize) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}