package com.example.cineai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineai.R
import com.example.cineai.data.model.Movie
import com.example.cineai.databinding.ItemMovieBinding
import com.example.cineai.ui.viewmodel.MovieViewModel

class MovieAdapter(private val movieViewModel: MovieViewModel) :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageViewStar.setOnClickListener {
                val movieId = getItem(bindingAdapterPosition)?.id ?: return@setOnClickListener
                handleStarClick(movieId.toString(), binding.imageViewStar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let {
            holder.binding.textViewMovieName.text = it.title
            holder.binding.textViewMovieStar.text = it.voteAverage.toString()
            holder.binding.textViewMovieOverview.text = it.overview
            Glide.with(holder.itemView.context)
                .load("https://image.tmdb.org/t/p/w500${it.posterPath}")
                .placeholder(R.drawable.image_32)
                .into(holder.binding.imageViewMovie)

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