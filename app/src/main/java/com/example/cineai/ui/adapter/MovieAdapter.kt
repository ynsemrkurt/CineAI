package com.example.cineai.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cineai.R
import com.example.cineai.data.model.Movie
import com.example.cineai.databinding.ItemMovieBinding
import com.example.cineai.ui.activity.DetailsActivity
import com.example.cineai.ui.classes.ImageSize
import com.example.cineai.ui.classes.loadImage
import com.example.cineai.ui.classes.toImageUrl
import com.example.cineai.ui.viewmodel.MovieViewModel
import java.util.Locale

class MovieAdapter(private val movieViewModel: MovieViewModel) :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val movieId = getItem(bindingAdapterPosition)?.id
                val intent = Intent(itemView.context, DetailsActivity::class.java)
                intent.putExtra("movieId", movieId.toString())
                startActivity(itemView.context, intent, null)
            }

            binding.imageViewStar.setOnClickListener {
                val movieId = getItem(bindingAdapterPosition)?.id
                handleStarClick(movieId.toString(), binding.imageViewStar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val fadeIn = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_in)
        holder.binding.root.startAnimation(fadeIn)

        val movie = getItem(position)
        movie?.let {
            with(holder.binding) {
                textViewMovieName.text = it.title
                textViewMovieStar.text = String.format(Locale.getDefault(), "%.1f", it.voteAverage)
                textViewMovieOverview.text = it.overview
                imageViewMovie.loadImage(it.posterPath.toImageUrl(ImageSize.W500))
            }

            movieViewModel.isMovieFavorite(it.id.toString()) { isFavorite ->
                holder.binding.imageViewStar.setImageResource(
                    if (isFavorite) R.drawable.filled_star_32 else R.drawable.star_32
                )
            }
        }
    }

    private fun handleStarClick(movieId: String, starImageView: ImageView) {
        movieViewModel.isMovieFavorite(movieId) { isFavorite ->
            if (isFavorite) {
                movieViewModel.removeMovieFromFavorites(movieId)
                starImageView.setImageResource(R.drawable.star_32)
            } else {
                movieViewModel.addMovieToFavorites(movieId)
                starImageView.setImageResource(R.drawable.filled_star_32)
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}