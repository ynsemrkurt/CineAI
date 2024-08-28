package com.example.cineai.ui.adapter

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.cineai.R
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
        return when (viewType) {
            TYPE_YOUTUBE_PLAYER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_youtube_player, parent, false)
                YouTubeViewHolder(view, lifecycle)
            }

            TYPE_IMAGE_VIEW -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image_view, parent, false)
                ImageViewHolder(view)
            }

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
        val activity = itemView.context as Activity
        val fullScreenLayout = activity.findViewById<FrameLayout>(R.id.fullScreenLayout)
        val viewPager2 = activity.findViewById<ViewPager2>(R.id.viewPagerMovie)

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

                    youTubePlayerView.addFullscreenListener(object : FullscreenListener {
                        override fun onEnterFullscreen(
                            fullscreenView: View,
                            exitFullscreen: () -> Unit
                        ) {
                            enterFullScreen(fullscreenView)
                        }

                        override fun onExitFullscreen() {
                            exitFullScreen()
                        }
                    })
                }
            }, iFramePlayerOptions)
        }

        fun enterFullScreen(fullscreenView: View) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            viewPager2.visibility = View.GONE
            fullScreenLayout.visibility = View.VISIBLE
            fullScreenLayout.addView(fullscreenView)
        }

        fun exitFullScreen() {
            viewPager2.visibility = View.VISIBLE
            fullScreenLayout.visibility = View.GONE
            fullScreenLayout.removeAllViews()
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewBackdrop)

        fun bind(item: ItemType.Image) {
            Glide.with(imageView).load("https://image.tmdb.org/t/p/original" + item.imageUrl)
                .placeholder(R.drawable.image_32)
                .into(imageView)
        }
    }
}

sealed class ItemType {
    data class YouTube(val videoId: String) : ItemType()
    data class Image(val imageUrl: String) : ItemType()
}