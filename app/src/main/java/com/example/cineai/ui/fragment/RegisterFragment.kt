package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.R
import com.example.cineai.databinding.FragmentRegisterBinding
import com.example.cineai.ui.classes.LoadingAnim
import com.example.cineai.ui.classes.openFragment
import com.example.cineai.ui.classes.showToast
import com.example.cineai.ui.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonListeners()
        observeRegistrationStatus()
    }

    private fun setButtonListeners() {
        binding.buttonRegister.setOnClickListener {
            LoadingAnim().showLoadingAnimation(
                binding.loadingAnimationView,
                binding.textViewRegister
            )
            registerUser()
        }

        binding.imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun registerUser() = with(binding) {
        registerViewModel.registerUser(
            editTextUserName.text.toString().trim(),
            editTextEmail.text.toString().trim(),
            editTextPassword.text.toString().trim()
        )
    }

    private fun observeRegistrationStatus() {
        registerViewModel.registrationStatus.observe(viewLifecycleOwner) { status ->
            requireContext().showToast(getString(status))
            if (status == R.string.registration_successful) {
                openFragment(R.id.fragmentContainerView, ProfileSetupFragment())
            }
            LoadingAnim().hideLoadingAnimation(
                binding.loadingAnimationView,
                binding.textViewRegister
            )
        }
    }
}