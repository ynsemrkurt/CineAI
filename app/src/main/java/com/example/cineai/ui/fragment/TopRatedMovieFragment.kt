package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.databinding.FragmentTopRatedMovieBinding
import com.example.cineai.ui.utility.ViewModelUtility
import com.example.cineai.ui.viewmodel.MovieViewModel

class TopRatedMovieFragment : Fragment() {

    private lateinit var binding: FragmentTopRatedMovieBinding
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopRatedMovieBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.fetchMovies(RetrofitClient.api.getTopRatedMovies())
        ViewModelUtility().observeMovies(
            movieViewModel,
            binding.recyclerViewList,
            viewLifecycleOwner
        )
    }
}