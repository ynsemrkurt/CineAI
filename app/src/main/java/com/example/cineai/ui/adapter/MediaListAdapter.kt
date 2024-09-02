package com.example.cineai.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cineai.databinding.ItemThumbnailBinding
import com.example.cineai.ui.classes.ImageSize
import com.example.cineai.ui.classes.ItemType
import com.example.cineai.ui.classes.loadImage
import com.example.cineai.ui.classes.toImageUrl

class MediaListAdapter(
    private val items: List<ItemType>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MediaListAdapter.MediaViewHolder>() {

    var selectedPosition = -1

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemThumbnailBinding.inflate(inflater, parent, false)
        return MediaViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(items[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = items.size

    inner class MediaViewHolder(
        private val binding: ItemThumbnailBinding,
        private val viewType: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemType, isSelected: Boolean) {
            when (viewType) {
                TYPE_YOUTUBE_PLAYER -> {
                    val youtubeItem = item as ItemType.YouTube
                    binding.imageViewBackdropThumbnail.loadImage("https://img.youtube.com/vi/${youtubeItem.videoId}/0.jpg")
                    binding.playButton.visibility = View.VISIBLE
                }

                TYPE_IMAGE_VIEW -> {
                    val imageItem = item as ItemType.Image
                    binding.imageViewBackdropThumbnail.loadImage(
                        imageItem.imageUrl.toImageUrl(
                            ImageSize.W300
                        )
                    )
                    binding.playButton.visibility = View.GONE
                }
            }
            changeStroke(isSelected)
            setClickListeners(bindingAdapterPosition)
        }

        private fun setClickListeners(position: Int) {
            binding.root.setOnClickListener {
                onItemClick(position)
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(selectedPosition)
            }
        }

        private fun changeStroke(isSelected: Boolean) {
            binding.cardViewThumbnail.strokeColor =
                if (isSelected) Color.GRAY else Color.TRANSPARENT
        }
    }
}