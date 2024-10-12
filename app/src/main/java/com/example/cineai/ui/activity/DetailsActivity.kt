package com.example.cineai.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.cineai.databinding.ActivityDetailsBinding
import com.example.cineai.ui.adapter.CharacterAdapter
import com.example.cineai.ui.adapter.MediaAdapter
import com.example.cineai.ui.adapter.MediaListAdapter
import com.example.cineai.ui.classes.ImageSize
import com.example.cineai.ui.classes.ItemType
import com.example.cineai.ui.classes.isInternetAvailable
import com.example.cineai.ui.classes.loadImage
import com.example.cineai.ui.classes.restartCurrentActivity
import com.example.cineai.ui.classes.showNoInternetDialog
import com.example.cineai.ui.classes.showToast
import com.example.cineai.ui.classes.toImageUrl
import com.example.cineai.ui.viewmodel.MovieViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: MovieViewModel by viewModels()
    private val items = mutableListOf<ItemType>()

    companion object {
        const val MOVIE_ID = "movieId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternetConnection()
        loadAds()
        backButtonListener()
        fetchMovieDetails()
        observeMovieDetails()
        observeCharacter()
        observeError()
        observeVideo()
    }

    private fun checkInternetConnection() {
        if (!isInternetAvailable()) {
            showNoInternetDialog { restartCurrentActivity() }
        }
    }

    private fun backButtonListener() {
        binding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    private fun observeError() {
        viewModel.error.observe(this) { errorMessage ->
            this.showToast(errorMessage.toString())
        }
    }

    private fun observeVideo() {
        viewModel.videoId.observe(this) { videoId ->
            if (!videoId.isNullOrEmpty()) {
                items.add(ItemType.YouTube(videoId))
            }
            observeMovieBackdrops()
        }
    }

    private fun observeMovieBackdrops() {
        viewModel.movieBackdrops.observe(this) { movieBackdrops ->
            if (movieBackdrops.isNotEmpty()) {
                for (backDrop in movieBackdrops) {
                    items.add(ItemType.Image(backDrop))
                }
                binding.viewPagerMovie.adapter = MediaAdapter(items, lifecycle)
                binding.recyclerViewThumbnail.adapter = MediaListAdapter(items) {
                    binding.viewPagerMovie.setCurrentItem(it, true)
                }
                updateThumbnails()
            }
        }
    }

    private fun fetchMovieDetails() {
        intent.getStringExtra(MOVIE_ID)?.let { movieId ->
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
            with(binding) {
                textViewMovieName.text = movie.title
                textViewMovieStar.text =
                    String.format(Locale.getDefault(), "%.1f", movie.voteAverage)
                textViewMovieOverview.text = movie.overview
                releasedDate.text = movie.releaseDate
                genreName.text = movie.genreIds.joinToString { it.name }
                status.text = movie.status
                imageViewMovie.loadImage(movie.posterPath.toImageUrl(ImageSize.W300))
            }
        }
    }

    private fun loadAds() {
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@DetailsActivity)
            withContext(Dispatchers.Main) {
                val adRequest = AdRequest.Builder().build()
                binding.adView.loadAd(adRequest)
            }
        }
    }

    private fun updateThumbnails() {
        binding.viewPagerMovie.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.recyclerViewThumbnail.smoothScrollToPosition(position)

                (binding.recyclerViewThumbnail.adapter as? MediaListAdapter)?.apply {
                    val previousSelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previousSelectedPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        })
    }
}