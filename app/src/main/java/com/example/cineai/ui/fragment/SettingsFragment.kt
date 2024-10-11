package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineai.R
import com.example.cineai.databinding.FragmentSettingsBinding
import com.example.cineai.ui.activity.LoginActivity
import com.example.cineai.ui.classes.navigateToActivity
import com.example.cineai.ui.classes.openFragment
import com.example.cineai.ui.classes.showToast
import com.example.cineai.ui.classes.showUserDeleteDialog
import com.example.cineai.ui.fragment.ProfileSetupFragment.Companion.LOAD_DATA_KEY
import com.example.cineai.ui.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

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

        binding.constraintLayoutChangeProfile.setOnClickListener {
            val args = Bundle().apply {
                putBoolean(LOAD_DATA_KEY, true)
            }
            openFragment(R.id.fragmentContainerView, ProfileSetupFragment(), args)
        }
    }

    private fun setSignOutButton() {
        binding.constraintLayoutSignOut.setOnClickListener {
            viewModel.signOut().observe(viewLifecycleOwner) { isSignedOut ->
                if (isSignedOut) {
                    requireContext().showToast(getString(R.string.successfully_signed_out))
                    activity?.navigateToActivity(LoginActivity::class.java)
                }
            }
        }
    }

    private fun setDeleteAccountButton() {
        binding.constraintLayoutDeleteAccount.setOnClickListener {
            showUserDeleteDialog(deleteAccount = { deleteAccount() })
        }
    }

    private fun deleteAccount() {
        viewModel.deleteAccount().observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                requireContext().showToast(getString(R.string.successfully_deleted_account))
                activity?.navigateToActivity(LoginActivity::class.java)
            }.onFailure {
                requireContext().showToast(getString(R.string.failed_to_delete_account))
            }
        }
    }
}