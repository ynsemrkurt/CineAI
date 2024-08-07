package com.example.cineai.data.network

import com.example.cineai.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDbService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int): MovieResponse
}