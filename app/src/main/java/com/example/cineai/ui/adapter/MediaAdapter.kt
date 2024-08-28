package com.example.cineai.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.cineai.R
import com.example.cineai.ui.classes.FullScreenHelper
import com.example.cineai.ui.classes.loadImage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MediaAdapter(
    private val items: List<ItemType>,
    private val lifecycle: Lifecycle
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_YOUTUBE_PLAYER = 0
        private const val TYPE_IMAGE_VIEW = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ItemType.YouTube -> TYPE_YOUTUBE_PLAYER
            is ItemType.Image -> TYPE_IMAGE_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_YOUTUBE_PLAYER -> YouTubeViewHolder(
                inflater.inflate(
                    R.layout.item_youtube_player,
                    parent,
                    false
                ), lifecycle
            )

            TYPE_IMAGE_VIEW -> ImageViewHolder(
                inflater.inflate(
                    R.layout.item_image_view,
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is YouTubeViewHolder -> holder.bind(items[position] as ItemType.YouTube)
            is ImageViewHolder -> holder.bind(items[position] as ItemType.Image)
        }
    }

    override fun getItemCount(): Int = items.size

    class YouTubeViewHolder(itemView: View, lifecycle: Lifecycle) :
        RecyclerView.ViewHolder(itemView) {
        private val youTubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.youtubePlayer)
        private var youTubePlayer: YouTubePlayer? = null

        init {
            lifecycle.addObserver(youTubePlayerView)
        }

        fun bind(item: ItemType.YouTube) {
            if (youTubePlayer == null) {
                initializeYouTubePlayer(item.videoId)
            } else {
                youTubePlayer?.cueVideo(item.videoId, 0f)
            }
        }

        private fun initializeYouTubePlayer(videoId: String) {
            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build()

            youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@YouTubeViewHolder.youTubePlayer = youTubePlayer
                    youTubePlayer.cueVideo(videoId, 0f)

                    val activity = itemView.context as Activity

                    youTubePlayerView.addFullscreenListener(object : FullscreenListener {
                        override fun onEnterFullscreen(
                            fullscreenView: View,
                            exitFullscreen: () -> Unit
                        ) {
                            FullScreenHelper.enterFullScreen(activity, fullscreenView)
                        }

                        override fun onExitFullscreen() {
                            FullScreenHelper.exitFullScreen(activity)
                        }
                    })
                }
            }, iFramePlayerOptions)
        }
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewBackdrop)

        fun bind(item: ItemType.Image) {
            imageView.loadImage("https://image.tmdb.org/t/p/original${item.imageUrl}")
        }
    }
}

sealed class ItemType {
    data class YouTube(val videoId: String) : ItemType()
    data class Image(val imageUrl: String) : ItemType()
}