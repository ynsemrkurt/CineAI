package com.example.cineai.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cineai.R
import com.example.cineai.data.model.Profile
import com.example.cineai.databinding.FragmentProfileSetupBinding
import com.example.cineai.ui.activity.MainActivity
import com.example.cineai.ui.classes.LoadingAnim
import com.example.cineai.ui.classes.showToast
import com.example.cineai.ui.viewmodel.ProfileSetupViewModel

class ProfileSetupFragment : Fragment() {

    private lateinit var binding: FragmentProfileSetupBinding
    private val profileSetupViewModel: ProfileSetupViewModel by activityViewModels()
    private var shouldLoadData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shouldLoadData = it.getBoolean("should_load_data", false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (shouldLoadData) profileSetupViewModel.loadProfileData()

        binding.buttonSave.setOnClickListener {
            LoadingAnim().showLoadingAnimation(binding.loadingAnimationView, binding.textViewSave)
            val profile = setProfileInfo()
            profileSetupViewModel.saveProfile(profile)
            observeProfileSetupStatus()
        }

        observeProfileData()
    }

    private fun observeProfileSetupStatus() {
        profileSetupViewModel.profileSetupStatus.observe(viewLifecycleOwner) { status ->
            showToast(getString(status))
            if (status == R.string.profile_setup_successful && shouldLoadData) {
                parentFragmentManager.popBackStack()
            } else if (status == R.string.profile_setup_successful) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            LoadingAnim().hideLoadingAnimation(binding.loadingAnimationView, binding.textViewSave)
        }
    }

    private fun observeProfileData() {
        profileSetupViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            binding.editTextQuestionStress.setText(profile.stress)
            binding.editTextQuestionProblemSolving.setText(profile.problemSolving)
            binding.editTextQuestionDecisionMaking.setText(profile.decisionMaking)
            binding.editTextQuestionTeamwork.setText(profile.teamwork)
            binding.editTextQuestionMovieGenres.setText(profile.movieGenres)
            binding.editTextQuestionMusic.setText(profile.music)
            binding.editTextQuestionHobbies.setText(profile.hobbies)
            binding.editTextQuestionTravel.setText(profile.travel)
        }
    }

    private fun setProfileInfo(): Profile {
        return Profile(
            stress = binding.editTextQuestionStress.text.trim().toString(),
            problemSolving = binding.editTextQuestionProblemSolving.text.trim().toString(),
            decisionMaking = binding.editTextQuestionDecisionMaking.text.trim().toString(),
            teamwork = binding.editTextQuestionTeamwork.text.trim().toString(),
            movieGenres = binding.editTextQuestionMovieGenres.text.trim().toString(),
            music = binding.editTextQuestionMusic.text.trim().toString(),
            hobbies = binding.editTextQuestionHobbies.text.trim().toString(),
            travel = binding.editTextQuestionTravel.text.trim().toString()
        )
    }
}