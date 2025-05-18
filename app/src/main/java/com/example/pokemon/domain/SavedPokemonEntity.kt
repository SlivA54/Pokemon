package com.example.pokemon.domain

import com.example.pokemon.data.local.SavedPokemon

data class SavedPokemonEntity(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: String
) {

}
