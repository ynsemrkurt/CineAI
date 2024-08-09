package com.example.cineai.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.R
import com.example.cineai.databinding.FragmentLoginBinding
import com.example.cineai.ui.activity.MainActivity
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
            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            if (status == requireContext().getString(R.string.login_successful)) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
        val inflater = LayoutInflater.from(context)
        val cardView = inflater.inflate(R.layout.item_forgot_password, null)
        builder.setView(cardView)
        val email = cardView.findViewById<EditText>(R.id.editTextMail)
        val buttonSend = cardView.findViewById<Button>(R.id.buttonSend)
        val dialog = builder.create()
        buttonSend.setOnClickListener {
            if (email.text.toString().trim().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_enter_your_email),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                loginViewModel.sendPasswordResetEmail(
                    email.text.toString().trim(),
                    requireContext(),
                    dialog
                )
            }
        }
        dialog.show()
    }
}