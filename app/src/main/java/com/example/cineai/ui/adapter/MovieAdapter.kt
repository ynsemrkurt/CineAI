package com.example.cineai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineai.R
import com.example.cineai.data.model.Movie
import com.example.cineai.databinding.ItemMovieBinding

class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.textViewMovieName.text = movie.title
        holder.binding.textViewMovieStar.text = movie.voteAverage.toString()
        holder.binding.textViewMovieOverview.text = movie.overview
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            .placeholder(R.drawable.image_32)
            .into(holder.binding.imageViewMovie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}