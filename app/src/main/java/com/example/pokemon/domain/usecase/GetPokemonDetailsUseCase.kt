package com.example.pokemon.domain.usecase

import com.example.pokemon.presentation.PokemonDetails
import com.example.pokemon.domain.PokemonRepository

class GetPokemonDetailsUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(id: Int): PokemonDetails = repository.getPokemonDetails(id)
}