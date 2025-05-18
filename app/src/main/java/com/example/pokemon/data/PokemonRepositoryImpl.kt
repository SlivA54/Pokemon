package com.example.pokemon.data

import com.example.pokemon.presentation.PokemonDetails
import com.example.pokemon.presentation.PokemonListItem
import com.example.pokemon.data.local.PokemonDao
import com.example.pokemon.data.mapper.toEntity
import com.example.pokemon.data.mapper.toSavedPokemon
import com.example.pokemon.domain.PokemonRepository
import com.example.pokemon.domain.SavedPokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val dao: PokemonDao,
    private val apiService: PokeApiService
) : PokemonRepository {

    override fun getSavedPokemons(): Flow<List<SavedPokemonEntity>> =
        dao.getAll().map { list -> list.map { it.toEntity() } }

    override suspend fun savePokemon(pokemon: SavedPokemonEntity) {
        dao.insert(pokemon.toSavedPokemon())
    }

    override suspend fun deletePokemon(pokemon: SavedPokemonEntity) {
        dao.delete(pokemon.toSavedPokemon())
    }

    override suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonListItem> {
        return apiService.getPokemonList(limit, offset).execute().body()?.results ?: emptyList()
    }

    override suspend fun getPokemonDetails(id: Int): PokemonDetails {
        return apiService.getPokemonDetails(id).execute().body() ?: throw Exception("Not found")
    }

    override suspend fun getPokemonTypes(): List<TypeItem> {
        return apiService.getTypeList().execute().body()?.results ?: emptyList()
    }

    override suspend fun getPokemonByType(typeName: String): List<PokemonListItem> {
        return apiService.getPokemonByType(typeName).execute().body()?.pokemon?.map { it.pokemon } ?: emptyList()
    }
}
