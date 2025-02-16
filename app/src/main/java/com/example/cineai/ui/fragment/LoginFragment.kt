package com.example.cineai.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.R
import com.example.cineai.databinding.FragmentLoginBinding
import com.example.cineai.databinding.ItemForgotPasswordBinding
import com.example.cineai.ui.activity.MainActivity
import com.example.cineai.ui.classes.LoadingAnim
import com.example.cineai.ui.classes.isValidEmail
import com.example.cineai.ui.classes.navigateToActivity
import com.example.cineai.ui.classes.openFragment
import com.example.cineai.ui.classes.showToast
import com.example.cineai.ui.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonListeners()
        observeLoginStatus()
    }

    private fun setButtonListeners() {
        binding.buttonLogin.setOnClickListener {
            LoadingAnim().showLoadingAnimation(binding.loadingAnimationView, binding.textViewLogin)
            loginUser()
        }

        binding.textViewForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        binding.imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun loginUser() = with(binding) {
        loginViewModel.loginUser(
            editTextEmail.text.toString().trim(),
            editTextPassword.text.toString().trim()
        )
    }

    private fun observeLoginStatus() {
        loginViewModel.loginStatus.observe(viewLifecycleOwner) { status ->
            requireContext().showToast(getString(status))
            when (status) {
                R.string.login_successful -> {
                    activity?.navigateToActivity(MainActivity::class.java)
                }

                R.string.profile_missing -> {
                    openFragment(R.id.fragmentContainerView, ProfileSetupFragment())
                }
            }
            LoadingAnim().hideLoadingAnimation(binding.loadingAnimationView, binding.textViewLogin)
        }
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
        val itemBinding = ItemForgotPasswordBinding.inflate(layoutInflater)
        val cardView = itemBinding.root
        builder.setView(cardView)
        val dialog = builder.create()

        itemBinding.buttonSend.setOnClickListener {
            val email = itemBinding.editTextMail.text.toString().trim()
            if (!email.isValidEmail()) {
                requireContext().showToast(getString(R.string.please_enter_your_email))
            } else {
                loginViewModel.sendPasswordResetEmail(email) {
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
}