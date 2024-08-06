package com.example.cineai.ui.utility

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.cineai.data.model.Movie
import com.example.cineai.ui.adapter.MovieAdapter
import com.example.cineai.ui.viewmodel.MovieViewModel

class ViewModelUtility() {
    fun observeMovies(viewModel: MovieViewModel, recyclerView: RecyclerView, lifecycleOwner: LifecycleOwner) {
        viewModel.movies.observe(lifecycleOwner) { movies ->
            setupRecyclerView(movies, recyclerView)
        }
    }

    private fun setupRecyclerView(movies: List<Movie>, recyclerView: RecyclerView) {
        val adapter = MovieAdapter(movies)
        recyclerView.adapter = adapter
    }
}