package com.example.pokemon.domain.usecase

import com.example.pokemon.presentation.PokemonListItem
import com.example.pokemon.domain.PokemonRepository

class GetPokemonByTypeUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(typeName: String): List<PokemonListItem> = repository.getPokemonByType(typeName)
}