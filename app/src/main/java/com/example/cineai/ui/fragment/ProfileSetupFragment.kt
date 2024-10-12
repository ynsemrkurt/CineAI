package com.example.cineai.ui.fragment

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
import com.example.cineai.ui.classes.navigateToActivity
import com.example.cineai.ui.classes.showToast
import com.example.cineai.ui.viewmodel.ProfileSetupViewModel

class ProfileSetupFragment : Fragment() {

    private lateinit var binding: FragmentProfileSetupBinding
    private val profileSetupViewModel: ProfileSetupViewModel by activityViewModels()
    private var shouldLoadData = false

    companion object {
        const val LOAD_DATA_KEY = "should_load_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shouldLoadData = it.getBoolean(LOAD_DATA_KEY, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (shouldLoadData) profileSetupViewModel.loadProfileData()
        setSaveButtonClickListener()
        observeProfileData()
    }

    private fun setSaveButtonClickListener() {
        binding.buttonSave.setOnClickListener {
            LoadingAnim().showLoadingAnimation(binding.loadingAnimationView, binding.textViewSave)
            val profile = setProfileInfo()
            profileSetupViewModel.saveProfile(profile)
            observeProfileSetupStatus()
        }
    }

    private fun observeProfileSetupStatus() {
        profileSetupViewModel.profileSetupStatus.observe(viewLifecycleOwner) { status ->
            requireContext().showToast(getString(status))
            if (status == R.string.profile_setup_successful && shouldLoadData) {
                parentFragmentManager.popBackStack()
            } else if (status == R.string.profile_setup_successful) {
                activity?.navigateToActivity(MainActivity::class.java)
            }
            LoadingAnim().hideLoadingAnimation(binding.loadingAnimationView, binding.textViewSave)
        }
    }

    private fun observeProfileData() {
        profileSetupViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            with(binding) {
                editTextQuestionStress.setText(profile.stress)
                editTextQuestionProblemSolving.setText(profile.problemSolving)
                editTextQuestionDecisionMaking.setText(profile.decisionMaking)
                editTextQuestionTeamwork.setText(profile.teamwork)
                editTextQuestionMovieGenres.setText(profile.movieGenres)
                editTextQuestionMusic.setText(profile.music)
                editTextQuestionHobbies.setText(profile.hobbies)
                editTextQuestionTravel.setText(profile.travel)
            }
        }
    }

    private fun setProfileInfo(): Profile = with(binding) {
        return Profile(
            stress = editTextQuestionStress.text.trim().toString(),
            problemSolving = editTextQuestionProblemSolving.text.trim().toString(),
            decisionMaking = editTextQuestionDecisionMaking.text.trim().toString(),
            teamwork = editTextQuestionTeamwork.text.trim().toString(),
            movieGenres = editTextQuestionMovieGenres.text.trim().toString(),
            music = editTextQuestionMusic.text.trim().toString(),
            hobbies = editTextQuestionHobbies.text.trim().toString(),
            travel = editTextQuestionTravel.text.trim().toString()
        )
    }
}