package com.example.pokemon.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_pokemons")
data class SavedPokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val types: String
)
