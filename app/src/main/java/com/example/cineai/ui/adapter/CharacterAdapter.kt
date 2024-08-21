package com.example.cineai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineai.R
import com.example.cineai.data.model.Character
import com.example.cineai.databinding.ItemCharacterBinding

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
            binding.textViewActorName.text = character.name
            binding.textViewCharacterName.text = character.character
            Glide.with(binding.imageViewMovie.context)
                .load("https://image.tmdb.org/t/p/w500${character.profilePath}")
                .placeholder(R.drawable.people_32)
                .into(binding.imageViewMovie)
        }
    }
}