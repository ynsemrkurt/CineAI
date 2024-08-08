package com.example.cineai.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cineai.R
import com.example.cineai.databinding.FragmentIntroBinding

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

        binding.buttonRegister.setOnClickListener{
            openFragment(RegisterFragment())
        }

        binding.buttonLogin.setOnClickListener{
            openFragment(LoginFragment())
        }
    }

    private fun openFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }
}