package com.example.pokemon

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.*


@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemon: SavedPokemon)

    @Query("SELECT * FROM saved_pokemons")
    fun getAll(): Flow<List<SavedPokemon>>
}

