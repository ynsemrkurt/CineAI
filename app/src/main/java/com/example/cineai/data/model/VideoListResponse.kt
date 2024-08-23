package com.example.cineai.data.model

import com.google.gson.annotations.SerializedName

data class VideoListResponse(
    @SerializedName("items")
    val items: List<VideoItem>
)

data class VideoItem(
    @SerializedName("snippet")
    val snippet: Snippet
)

data class Snippet(
    @SerializedName("thumbnails")
    val thumbnails: Thumbnails
)

data class Thumbnails(
    @SerializedName("maxres")
    val maxres: Thumbnail
)

data class Thumbnail(
    @SerializedName("url")
    val url: String
)
