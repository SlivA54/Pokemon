package com.example.pokemon.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemon: SavedPokemon)

    @Query("SELECT * FROM saved_pokemons")
    fun getAll(): Flow<List<SavedPokemon>>

    @Delete
    fun delete(pokemon: SavedPokemon)
}

