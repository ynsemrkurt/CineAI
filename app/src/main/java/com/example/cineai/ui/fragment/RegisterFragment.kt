package com.example.cineai.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.R
import com.example.cineai.databinding.FragmentRegisterBinding
import com.example.cineai.ui.activity.MainActivity
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

        binding.buttonRegister.setOnClickListener {
            registerUser()
        }
        observeRegistrationStatus()
    }

    private fun registerUser() = with(binding) {
        registerViewModel.registerUser(
            editTextUserName.text.toString().trim(),
            editTextEmail.text.toString().trim(),
            editTextPassword.text.toString().trim(),
            requireContext()
        )
    }

    private fun observeRegistrationStatus() {
        registerViewModel.registrationStatus.observe(viewLifecycleOwner) { status ->
            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            if (status == requireContext().getString(R.string.registration_successful)) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                activity?.finish()
            }
        }
    }
}