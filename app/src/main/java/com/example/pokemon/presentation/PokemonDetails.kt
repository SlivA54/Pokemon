package com.example.pokemon.presentation

data class PokemonDetails(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val abilities: List<AbilitySlot>,
    val stats: List<StatSlot>,
    val height: Int,
    val weight: Int,
    val moves: List<MoveSlot>,
    val species: Species
)

data class Sprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    // другие спрайты по желанию
)

data class TypeSlot(val slot: Int, val type: Type)
data class Type(val name: String)

data class AbilitySlot(val is_hidden: Boolean, val ability: Ability)
data class Ability(val name: String)

data class StatSlot(val base_stat: Int, val stat: Stat)
data class Stat(val name: String)

data class MoveSlot(val move: Move)
data class Move(val name: String)

data class Species(val name: String, val url: String)


