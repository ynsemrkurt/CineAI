package com.example.cineai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.cineai.databinding.FragmentImageDialogBinding

class ImageDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentImageDialogBinding
    private var imageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Close button
        binding.imageButtonClose.setOnClickListener {
            dismiss()
        }

        // Load image into PhotoView
        imageUrl?.let {
            Glide.with(this)
                .load(it)
                .into(binding.photoView)
        }

        // Download button
        binding.imageButtonDownload.setOnClickListener {
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun setImageUrl(url: String) {
        this.imageUrl = url
    }
}