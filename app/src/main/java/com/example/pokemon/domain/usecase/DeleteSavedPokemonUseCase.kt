package com.example.pokemon.domain.usecase

import com.example.pokemon.domain.PokemonRepository
import com.example.pokemon.domain.SavedPokemonEntity

class DeleteSavedPokemonUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(pokemon: SavedPokemonEntity) = repository.deletePokemon(pokemon)
}