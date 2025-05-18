package com.example.pokemon.domain.usecase

import com.example.pokemon.data.TypeItem
import com.example.pokemon.domain.PokemonRepository

class GetPokemonTypesUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(): List<TypeItem> = repository.getPokemonTypes()
}