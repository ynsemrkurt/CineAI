package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineai.data.network.RetrofitClient
import com.example.cineai.databinding.FragmentRecommendationDialogBinding
import com.example.cineai.ui.adapter.MovieAdapter
import com.example.cineai.ui.viewmodel.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRecommendationDialogBinding
    private lateinit var movieAdapter: MovieAdapter
    private var recommendations: List<String>? = null

    companion object {
        private const val ARG_RECOMMENDATIONS = "recommendations"

        fun newInstance(recommendations: List<String>): RecommendationDialogFragment {
            val fragment = RecommendationDialogFragment()
            val args = Bundle().apply {
                putStringArrayList(ARG_RECOMMENDATIONS, ArrayList(recommendations))
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendationDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recommendations = arguments?.getStringArrayList(ARG_RECOMMENDATIONS)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val movieViewModel = MovieViewModel()

        movieAdapter = MovieAdapter(movieViewModel)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = movieAdapter

        fetchMovies()
    }

    private fun fetchMovies() {
        val movieNames = recommendations ?: return

        CoroutineScope(Dispatchers.IO).launch {
            val movies = movieNames.mapNotNull { movieName ->
                try {
                    RetrofitClient.api.searchMovies(movieName).results.find { it.title == movieName }
                } catch (e: Exception) {
                    null
                }
            }

            withContext(Dispatchers.Main) {
                movieAdapter.submitData(PagingData.from(movies))
            }
        }
    }
}