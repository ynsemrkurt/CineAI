package com.example.cineai.data.network

import com.example.cineai.data.model.CharacterResponse
import com.example.cineai.data.model.Movie
import com.example.cineai.data.model.MovieResponse
import com.example.cineai.data.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbService {
    @GET(popularMovies)
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET(topRatedMovies)
    suspend fun getTopRatedMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET(nowPlayingMovies)
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET(upcomingMovies)
    suspend fun getUpcomingMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET(movieDetails)
    suspend fun getDetailsMovies(
        @Path("movie_id") movieId: String
    ): Movie

    @GET(searchMovies)
    suspend fun searchMovies(
        @Query("query") query: String
    ): MovieResponse

    @GET(searchVideo)
    suspend fun searchVideo(
        @Path("movie_id") movieId: String
    ): VideoResponse

    @GET(searchCharacter)
    suspend fun searchCharacter(
        @Path("movie_id") movieId: String
    ): CharacterResponse
}

private const val popularMovies = "movie/popular"
private const val topRatedMovies = "movie/top_rated"
private const val nowPlayingMovies = "movie/now_playing"
private const val upcomingMovies = "movie/upcoming"
private const val movieDetails = "movie/{movie_id}"
private const val searchMovies = "search/movie"
private const val searchVideo = "movie/{movie_id}/videos"
private const val searchCharacter = "movie/{movie_id}/credits"