package com.example.pokemon.domain

import com.example.pokemon.presentation.PokemonDetails
import com.example.pokemon.presentation.PokemonListItem
import com.example.pokemon.data.TypeItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getSavedPokemons(): Flow<List<SavedPokemonEntity>>
    suspend fun savePokemon(pokemon: SavedPokemonEntity)
    suspend fun deletePokemon(pokemon: SavedPokemonEntity)

    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonListItem>
    suspend fun getPokemonDetails(id: Int): PokemonDetails
    suspend fun getPokemonTypes(): List<TypeItem>
    suspend fun getPokemonByType(typeName: String): List<PokemonListItem>
}
