package com.example.cineai.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results")
    val results: List<Movie>
)

data class VideoResponse(
    @SerializedName("results")
    val results: List<Video>
)

data class Video(
    @SerializedName("key")
    val key: String,
    @SerializedName("type")
    val type: String,
)

data class Movie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("genres")
    val genreIds: List<Genre>,
    @SerializedName("status")
    val status: String,
)

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)