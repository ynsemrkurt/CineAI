package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.example.cineai.databinding.FragmentRecommendationDialogBinding
import com.example.cineai.ui.adapter.MovieAdapter
import com.example.cineai.ui.viewmodel.AiRecommendationViewModel
import com.example.cineai.ui.viewmodel.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class RecommendationDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRecommendationDialogBinding
    private val aiRecommendationViewModel: AiRecommendationViewModel by viewModels()

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

        val movieAdapter = MovieAdapter(MovieViewModel())
        binding.list.adapter = movieAdapter

        setMovies()
        observeMovies(movieAdapter)
    }

    private fun setMovies() {
        arguments?.getStringArrayList(ARG_RECOMMENDATIONS)?.let {
            aiRecommendationViewModel.fetchMovies(it)
        }
    }

    private fun observeMovies(adapter: MovieAdapter) {
        aiRecommendationViewModel.movies.observe(viewLifecycleOwner) { movieList ->
            lifecycleScope.launch {
                adapter.submitData(PagingData.from(movieList))
            }
        }
    }
}