package com.example.pokemon.domain.usecase

import com.example.pokemon.presentation.PokemonListItem
import com.example.pokemon.domain.PokemonRepository

class GetPokemonListUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(limit: Int, offset: Int): List<PokemonListItem> =
        repository.getPokemonList(limit, offset)
}