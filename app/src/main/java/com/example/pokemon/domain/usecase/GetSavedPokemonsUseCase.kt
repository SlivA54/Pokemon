package com.example.pokemon.domain.usecase

import com.example.pokemon.domain.PokemonRepository
import com.example.pokemon.domain.SavedPokemonEntity
import kotlinx.coroutines.flow.Flow

class GetSavedPokemonsUseCase(private val repository: PokemonRepository) {
    operator fun invoke(): Flow<List<SavedPokemonEntity>> = repository.getSavedPokemons()
}