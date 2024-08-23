package com.example.cineai.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cineai.R
import com.example.cineai.databinding.FragmentSettingsBinding
import com.example.cineai.ui.activity.LoginActivity
import com.example.cineai.ui.classes.showToast
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSignOutButton()
        setDeleteAccountButton()
    }

    private fun setSignOutButton() {
        binding.constraintLayoutSignOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            showToast(getString(R.string.successfully_signed_out))
            requireActivity().finish()
        }
    }

    private fun setDeleteAccountButton() {
        binding.constraintLayoutDeleteAccount.setOnClickListener {
            firebaseAuth.currentUser?.delete()?.let { task ->
                task.addOnSuccessListener {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    showToast(getString(R.string.successfully_deleted_account))
                    requireActivity().finish()
                }
                task.addOnFailureListener {
                    showToast(getString(R.string.failed_to_delete_account))
                }
            }
        }
    }
}