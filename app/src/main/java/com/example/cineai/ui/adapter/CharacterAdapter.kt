package com.example.cineai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cineai.R
import com.example.cineai.data.model.Character
import com.example.cineai.databinding.ItemCharacterBinding
import com.example.cineai.ui.classes.loadImage

class CharacterAdapter(private val characters: List<Character>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            with(binding) {
                textViewActorName.text = character.name
                textViewCharacterName.text = character.character
                imageViewMovie.loadImage(
                    "https://image.tmdb.org/t/p/w300${character.profilePath}",
                    R.drawable.people_32
                )
            }
        }
    }
}