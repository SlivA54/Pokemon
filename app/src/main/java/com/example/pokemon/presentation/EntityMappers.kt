package com.example.pokemon.data.mapper

import com.example.pokemon.data.local.SavedPokemon
import com.example.pokemon.domain.SavedPokemonEntity


fun SavedPokemon.toEntity() = SavedPokemonEntity(id, name, imageUrl, types)
fun SavedPokemonEntity.toSavedPokemon() = SavedPokemon(id, name, imageUrl, types)
