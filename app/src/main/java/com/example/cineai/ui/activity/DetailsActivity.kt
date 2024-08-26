package com.example.cineai.ui.activity

import android.os.Bundle
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
        observeCharacter()
        observeError()

        binding.imageViewBack.setOnClickListener {
            finish()
        }
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