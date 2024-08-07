package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineai.databinding.FragmentNowPlayingMovieBinding
import com.example.cineai.ui.adapter.MovieAdapter
import com.example.cineai.ui.viewmodel.MovieViewModel
import com.example.cineai.ui.viewmodel.MovieViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NowPlayingMovieFragment : Fragment() {

    private lateinit var binding: FragmentNowPlayingMovieBinding
    private val movieViewModel: MovieViewModel by viewModels { MovieViewModelFactory("now_playing") }
    private val movieAdapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNowPlayingMovieBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewList.adapter = movieAdapter

        lifecycleScope.launch {
            movieViewModel.getMovies().collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }
}