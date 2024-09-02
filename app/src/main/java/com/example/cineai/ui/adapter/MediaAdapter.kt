package com.example.cineai.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.cineai.databinding.ItemImageViewBinding
import com.example.cineai.databinding.ItemYoutubePlayerBinding
import com.example.cineai.ui.classes.FullScreenHelper
import com.example.cineai.ui.classes.ImageSize
import com.example.cineai.ui.classes.ItemType
import com.example.cineai.ui.classes.loadImage
import com.example.cineai.ui.classes.toImageUrl
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

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
                ItemYoutubePlayerBinding.inflate(inflater, parent, false),
                lifecycle
            )

            TYPE_IMAGE_VIEW -> ImageViewHolder(
                ItemImageViewBinding.inflate(inflater, parent, false)
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

    class YouTubeViewHolder(
        private val binding: ItemYoutubePlayerBinding,
        lifecycle: Lifecycle
    ) : RecyclerView.ViewHolder(binding.root) {
        private var youTubePlayer: YouTubePlayer? = null

        init {
            lifecycle.addObserver(binding.youtubePlayer)
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

            binding.youtubePlayer.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@YouTubeViewHolder.youTubePlayer = youTubePlayer
                    youTubePlayer.cueVideo(videoId, 0f)

                    val activity = itemView.context as Activity

                    binding.youtubePlayer.addFullscreenListener(object : FullscreenListener {
                        override fun onEnterFullscreen(
                            fullscreenView: View,
                            exitFullscreen: () -> Unit
                        ) {
                            FullScreenHelper.enterFullScreen(activity, fullscreenView)
                            youTubePlayer.play()
                        }

                        override fun onExitFullscreen() {
                            FullScreenHelper.exitFullScreen(activity)
                        }
                    })
                }
            }, iFramePlayerOptions)
        }
    }

    class ImageViewHolder(
        private val binding: ItemImageViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemType.Image) {
            binding.imageViewBackdrop.loadImage(item.imageUrl.toImageUrl(ImageSize.ORIGINAL))
        }
    }
}