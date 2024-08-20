package com.example.cineai.ui.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MovieCategory(val value: String) : Parcelable {
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    NOW_PLAYING("now_playing"),
    UPCOMING("upcoming"),
    FAVORITE("favorite"),
}