package com.example.pokemon

data class PokemonDetails(
    val sprites: Sprites,
    val types: List<TypeSlot>
)

data class Sprites(
    val front_default: String?
)

data class TypeSlot(
    val type: Type
)

data class Type(
    val name: String
)
