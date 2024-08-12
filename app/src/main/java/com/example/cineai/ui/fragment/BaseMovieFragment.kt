package com.example.cineai.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineai.databinding.FragmentBaseMovieBinding
import com.example.cineai.ui.adapter.MovieAdapter
import com.example.cineai.ui.classes.MovieCategory
import com.example.cineai.ui.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BaseMovieFragment : Fragment() {

    private lateinit var binding: FragmentBaseMovieBinding
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieCategory: MovieCategory

    companion object {
        private const val ARG_CATEGORY = "movieCategory"

        fun newInstance(movieCategory: MovieCategory): BaseMovieFragment {
            return BaseMovieFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("movieCategory", movieCategory)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieCategory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable(ARG_CATEGORY, MovieCategory::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                arguments?.getSerializable(ARG_CATEGORY) as MovieCategory
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseMovieBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieAdapter = MovieAdapter(movieViewModel)
        binding.recyclerViewList.adapter = movieAdapter

        if (movieCategory == MovieCategory.FAVORITE) {
            movieViewModel.loadFavoriteMovieIds()
            observeFavoriteMovieIds(movieAdapter)
        } else {
            getMovies(movieAdapter)
        }
    }

    private fun observeFavoriteMovieIds(adapter: MovieAdapter) {
        movieViewModel.favoriteMovieIds.observe(viewLifecycleOwner) { favoriteMovieIds ->
            getMovies(adapter, favoriteMovieIds)
        }
    }

    private fun getMovies(movieAdapter: MovieAdapter, favList: List<String>? = null) {
        lifecycleScope.launch {
            movieViewModel.getMovies(movieCategory.value, favList).collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }
}
