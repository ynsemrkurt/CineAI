package com.example.cineai.data.network

import com.example.cineai.data.model.CharacterResponse
import com.example.cineai.data.model.Movie
import com.example.cineai.data.model.MovieBackdrops
import com.example.cineai.data.model.MovieResponse
import com.example.cineai.data.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbService {
    @GET(POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET(TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET(NOW_PLAYING_MOVIES)
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET(UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET(MOVIE_DETAILS)
    suspend fun getDetailsMovies(
        @Path("movie_id") movieId: String
    ): Movie

    @GET(SEARCH_MOVIES)
    suspend fun searchMovies(
        @Query("query") query: String
    ): MovieResponse

    @GET(SEARCH_VIDEO)
    suspend fun searchVideo(
        @Path("movie_id") movieId: String
    ): VideoResponse

    @GET(SEARCH_CHARACTER)
    suspend fun searchCharacter(
        @Path("movie_id") movieId: String
    ): CharacterResponse

    @GET(MOVIE_BACKDROPS)
    suspend fun getMovieBackdrops(
        @Path("movie_id") movieId: String
    ): MovieBackdrops
}

private const val POPULAR_MOVIES = "movie/popular"
private const val TOP_RATED_MOVIES = "movie/top_rated"
private const val NOW_PLAYING_MOVIES = "movie/now_playing"
private const val UPCOMING_MOVIES = "movie/upcoming"
private const val MOVIE_DETAILS = "movie/{movie_id}"
private const val SEARCH_MOVIES = "search/movie"
private const val SEARCH_VIDEO = "movie/{movie_id}/videos"
private const val SEARCH_CHARACTER = "movie/{movie_id}/credits"
private const val MOVIE_BACKDROPS = "movie/{movie_id}/images"
