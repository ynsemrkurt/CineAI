package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cineai.R
import com.example.cineai.databinding.FragmentIntroBinding
import com.example.cineai.ui.classes.openFragment

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding.buttonRegister.setOnClickListener {
            openFragment(R.id.fragmentContainerView, RegisterFragment())
        }

        binding.buttonLogin.setOnClickListener {
            openFragment(R.id.fragmentContainerView, LoginFragment())
        }
    }
}