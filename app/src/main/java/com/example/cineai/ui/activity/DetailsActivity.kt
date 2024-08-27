package com.example.cineai.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cineai.databinding.ActivityDetailsBinding
import com.example.cineai.ui.adapter.CharacterAdapter
import com.example.cineai.ui.viewmodel.MovieViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var youTubePlayer: YouTubePlayer
    private var isFullscreen = false

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                youTubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        loadAds()
        getMovieId()
        observeMovieDetails()
        observeCharacter()
        observeError()
        observeVideo()

        binding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    private fun observeError() {
        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeVideo() {
        viewModel.videoId.observe(this) { videoId ->
            setYoutubePlayer(videoId)
        }
    }

    private fun setYoutubePlayer(videoId: String) {
        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1)
            .build()

        binding.youtubePlayer.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
                binding.youtubePlayer.visibility = View.GONE
                binding.fullScreenLayout.visibility = View.VISIBLE
                binding.fullScreenLayout.addView(fullscreenView)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onExitFullscreen() {
                isFullscreen = false
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                binding.youtubePlayer.visibility = View.VISIBLE
                binding.fullScreenLayout.visibility = View.GONE
                binding.fullScreenLayout.removeAllViews()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        })

        binding.youtubePlayer.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@DetailsActivity.youTubePlayer = youTubePlayer
                youTubePlayer.cueVideo(videoId, 0f)
            }
        }, iFramePlayerOptions)

        lifecycle.addObserver(binding.youtubePlayer)
    }

    private fun getMovieId() {
        intent.getStringExtra("movieId")?.let { movieId ->
            viewModel.fetchMovieDetails(movieId)
        }
    }

    private fun observeCharacter() {
        viewModel.character.observe(this) { character ->
            val adapter = CharacterAdapter(character.cast)
            binding.recyclerViewCharacters.adapter = adapter
        }
    }

    private fun observeMovieDetails() {
        viewModel.movieDetails.observe(this) { movie ->
            binding.textViewMovieName.text = movie.title
            binding.textViewMovieStar.text = movie.voteAverage.toString()
            binding.textViewMovieOverview.text = movie.overview
            binding.releasedDate.text = movie.releaseDate
            binding.genreName.text = movie.genreIds.joinToString { it.name }
            binding.status.text = movie.status
            Glide.with(this).load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(binding.imageViewMovie)
            Glide.with(this).load("https://image.tmdb.org/t/p/original${movie.backdropPath}")
                .into(binding.imageViewBackdrop)
        }
    }

    private fun loadAds() {
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@DetailsActivity)
        }
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }
}