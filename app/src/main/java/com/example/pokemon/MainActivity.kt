package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedPokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.savedPokemonsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SavedPokemonAdapter { pokemon ->
            val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                putExtra("pokemon_id", pokemon.id)
                putExtra("pokemon_name", pokemon.name)
                putExtra("pokemon_image_url", pokemon.imageUrl)
                putExtra("pokemon_types", pokemon.types)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        loadSavedPokemons()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadSavedPokemons() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            db.pokemonDao().getAll().collect { savedPokemonsList ->
                adapter.submitList(savedPokemonsList)
            }
        }
    }
}
