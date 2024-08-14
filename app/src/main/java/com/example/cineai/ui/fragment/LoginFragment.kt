package com.example.cineai.ui.fragment

import android.app.AlertDialog
import android.content.Intent
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
import com.example.cineai.ui.classes.showToast
import com.example.cineai.ui.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var itemBinding: ItemForgotPasswordBinding
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

        binding.buttonLogin.setOnClickListener {
            loginUser()
        }
        binding.textViewForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        observeLoginStatus()
    }

    private fun loginUser() = with(binding) {
        loginViewModel.loginUser(
            editTextEmail.text.toString().trim(),
            editTextPassword.text.toString().trim(),
            requireContext()
        )
    }

    private fun observeLoginStatus() {
        loginViewModel.loginStatus.observe(viewLifecycleOwner) { status ->
            showToast(status)
            if (status == requireContext().getString(R.string.login_successful)) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
        itemBinding = ItemForgotPasswordBinding.inflate(layoutInflater)
        val cardView = itemBinding.root
        builder.setView(cardView)
        val dialog = builder.create()
        itemBinding.buttonSend.setOnClickListener {
            if (itemBinding.editTextMail.text.toString().trim().isEmpty()) {
                showToast(getString(R.string.please_enter_your_email))
            } else {
                loginViewModel.sendPasswordResetEmail(
                    itemBinding.editTextMail.text.toString().trim(),
                    requireContext(),
                    dialog
                )
            }
        }
        dialog.show()
    }
}