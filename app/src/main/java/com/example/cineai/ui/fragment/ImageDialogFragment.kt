package com.example.cineai.ui.fragment

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.cineai.R
import com.example.cineai.databinding.FragmentImageDialogBinding
import com.example.cineai.ui.classes.showToast

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

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.imageButtonClose.setOnClickListener {
            dismiss()
        }

        imageUrl?.let {
            Glide.with(this)
                .load(it)
                .into(binding.photoView)
        }

        binding.imageButtonDownload.setOnClickListener {
            imageUrl?.let {
                downloadImage(it, it.substringAfterLast("/"))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun setImageUrl(url: String) {
        this.imageUrl = url
    }

    private fun downloadImage(imageUrl: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle(getString(R.string.downloading_image))
            .setDescription(getString(R.string.downloading_image_from, imageUrl))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                getString(R.string.jpg, fileName))

        val downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        requireContext().showToast(getString(R.string.image_downloading))
    }
}