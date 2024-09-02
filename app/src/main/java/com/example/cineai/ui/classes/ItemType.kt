package com.example.cineai.ui.classes

sealed class ItemType {
    data class YouTube(val videoId: String) : ItemType()
    data class Image(val imageUrl: String) : ItemType()
}