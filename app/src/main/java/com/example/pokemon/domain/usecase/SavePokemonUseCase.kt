package com.example.pokemon.domain.usecase

import com.example.pokemon.domain.PokemonRepository
import com.example.pokemon.domain.SavedPokemonEntity

class SavePokemonUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(pokemon: SavedPokemonEntity) = repository.savePokemon(pokemon)
}