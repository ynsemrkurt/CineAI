package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.databinding.FragmentAiRecommendationBinding
import com.example.cineai.ui.classes.LoadingAnim
import com.example.cineai.ui.classes.showToast
import com.example.cineai.ui.viewmodel.AiRecommendationViewModel

class AiRecommendationFragment : Fragment() {

    private lateinit var binding: FragmentAiRecommendationBinding
    private val viewModel: AiRecommendationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAiRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchAndRecommendMovies()
        observeRecommendationStatus()
        observeError()

        binding.buttonCreate.setOnClickListener {
            LoadingAnim().showLoadingAnimation(
                binding.loadingAnimationView,
                binding.textViewRecommendation
            )
            recommendMovies()
        }
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner) { messageResId ->
            requireContext().showToast(getString(messageResId))
        }
    }

    private fun observeRecommendationStatus() {
        viewModel.recommendationStatus.observe(viewLifecycleOwner) { messageResId ->
            requireContext().showToast(getString(messageResId))
        }
        LoadingAnim().hideLoadingAnimation(
            binding.loadingAnimationView,
            binding.textViewRecommendation
        )
    }

    private fun recommendMovies() {
        viewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            if (recommendations.isNotEmpty()) {
                val bottomSheet =
                    RecommendationDialogFragment.newInstance(recommendations.split("\n"))
                bottomSheet.show(childFragmentManager, "ModalBottomSheet")
            }
            LoadingAnim().hideLoadingAnimation(
                binding.loadingAnimationView,
                binding.textViewRecommendation
            )
        }
    }
}