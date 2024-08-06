package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.databinding.FragmentUpcomingMovieBinding
import com.example.cineai.ui.utility.ViewModelUtility
import com.example.cineai.ui.viewmodel.MovieViewModel

class UpcomingMovieFragment : Fragment() {

    private lateinit var binding: FragmentUpcomingMovieBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingMovieBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchMovies(RetrofitClient.api.getUpcomingMovies())
        ViewModelUtility().observeMovies(
            viewModel,
            binding.recyclerViewList,
            viewLifecycleOwner
        )
    }
}