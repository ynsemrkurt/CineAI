package com.example.cineai.ui.fragment

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
import com.example.cineai.ui.classes.getParcelable
import com.example.cineai.ui.classes.putParcelable
import com.example.cineai.ui.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BaseMovieFragment : Fragment() {

    private lateinit var binding: FragmentBaseMovieBinding
    private val movieViewModel: MovieViewModel by viewModels()
    private val movieAdapter = MovieAdapter()
    private lateinit var movieCategory: MovieCategory

    companion object {
        private const val ARG_CATEGORY = "movieCategory"

        fun newInstance(movieCategory: MovieCategory): BaseMovieFragment {
            return BaseMovieFragment().apply {
                putParcelable(ARG_CATEGORY, movieCategory)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieCategory =
            getParcelable(ARG_CATEGORY, MovieCategory::class.java) ?: MovieCategory.POPULAR
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

        binding.recyclerViewList.adapter = movieAdapter
        getMovies()
    }

    private fun getMovies() {
        lifecycleScope.launch {
            movieViewModel.getMovies(movieCategory.value).collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }
}