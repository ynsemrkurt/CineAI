package com.example.cineai.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.R
import com.example.cineai.data.model.Movie
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.databinding.FragmentListBinding
import com.example.cineai.ui.adapter.MovieAdapter
import com.example.cineai.ui.viewmodel.MovieViewModel
import com.google.android.material.tabs.TabLayout

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setList()
        observeMovies()
        observeErrorMessage()
    }

    private fun setList() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                movieViewModel.fetchMovies(
                    when (tab?.position) {
                        0 -> RetrofitClient.api.getPopularMovies()
                        1 -> RetrofitClient.api.getTopRatedMovies()
                        2 -> RetrofitClient.api.getNowPlayingMovies()
                        3 -> RetrofitClient.api.getUpcomingMovies()
                        else -> RetrofitClient.api.getPopularMovies()
                    }
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeMovies() {
        movieViewModel.movies.observe(viewLifecycleOwner) { movies ->
            setupRecyclerView(movies)
        }
    }

    private fun setupRecyclerView(movies: List<Movie>) {
        val adapter = MovieAdapter(movies)
        binding.recyclerViewList.adapter = adapter
    }

    private fun observeErrorMessage() {
        movieViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.error))
                .setMessage(getString(R.string.error_dialog, errorMessage))
                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}