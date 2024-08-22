package com.example.cineai.ui.activity

import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cineai.databinding.ActivityDetailsBinding
import com.example.cineai.ui.adapter.CharacterAdapter
import com.example.cineai.ui.viewmodel.MovieViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadAds()
        getMovieId()
        observeMovieDetails()
        observeVideo()
        observeCharacter()
        observeError()
    }

    private fun observeError() {
        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
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

    private fun observeVideo() {
        viewModel.video.observe(this) {
            val video = """
                <html>
                <body style="margin:0;padding:0;">
                    <iframe width="100%" height="100%" src="https://www.youtube.com/embed/$it?si=tIzzHIfnlQexEd2z" 
                    title="YouTube video player" frameborder="0" 
                    style="border: none; margin: 0; padding: 0;"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
                    referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
                </body>
                </html>
            """
            binding.webView.loadData(video, "text/html", "utf-8")
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.setWebViewClient(WebViewClient())
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
            Glide.with(this).load("https://image.tmdb.org/t/p/w500${movie.backdropPath}")
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