package com.example.cineai.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.example.cineai.databinding.FragmentSearchBinding
import com.example.cineai.ui.activity.DetailsActivity
import com.example.cineai.ui.adapter.MovieAdapter
import com.example.cineai.ui.viewmodel.MovieViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        observeMovies()
    }

    private fun setClickListeners() {
        binding.imageButtonSearch.setOnClickListener {
            searchMovies()
        }

        binding.editTextSearch.setOnEditorActionListener { _, _, _ ->
            searchMovies()
            true
        }
    }

    private fun searchMovies() {
        val query = binding.editTextSearch.text.toString().trim()
        if (query.isNotEmpty() && query.length >= 3) {
            viewModel.searchMovies(query)
        }
    }

    private fun observeMovies() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isNotEmpty()) {
                binding.recyclerViewSearch.visibility = View.VISIBLE
                binding.layoutNoData.visibility = View.GONE

                val movieAdapter = MovieAdapter(
                    isMovieFavorite = { movieId, callback ->
                        viewModel.isMovieFavorite(movieId, callback)
                    },
                    addMovieToFavorites = { movieId ->
                        viewModel.addMovieToFavorites(movieId)
                    },
                    removeMovieFromFavorites = { movieId ->
                        viewModel.removeMovieFromFavorites(movieId)
                    },
                    onMovieClick = { movieId ->
                        val intent = Intent(requireContext(), DetailsActivity::class.java)
                        intent.putExtra(DetailsActivity.MOVIE_ID, movieId)
                        startActivity(intent)
                    }
                )
                binding.recyclerViewSearch.adapter = movieAdapter
                lifecycleScope.launch {
                    movieAdapter.submitData(PagingData.from(movies))
                }
            } else {
                binding.recyclerViewSearch.visibility = View.GONE
                binding.layoutNoData.visibility = View.VISIBLE
            }
        }
    }
}