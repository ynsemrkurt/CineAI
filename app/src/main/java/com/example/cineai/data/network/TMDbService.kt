package com.example.cineai.data.network

import com.example.cineai.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDbService {
    @GET(popularMovies)
    suspend fun getPopularMovies(@Query("page") page: Int): MovieResponse

    @GET(topRatedMovies)
    suspend fun getTopRatedMovies(@Query("page") page: Int): MovieResponse

    @GET(nowPlayingMovies)
    suspend fun getNowPlayingMovies(@Query("page") page: Int): MovieResponse

    @GET(upcomingMovies)
    suspend fun getUpcomingMovies(@Query("page") page: Int): MovieResponse
}

private const val popularMovies = "movie/popular"
private const val topRatedMovies = "movie/top_rated"
private const val nowPlayingMovies = "movie/now_playing"
private const val upcomingMovies = "movie/upcoming"