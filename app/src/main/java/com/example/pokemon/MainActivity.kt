package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.first


class MainActivity : AppCompatActivity() {

    private lateinit var pokeApiService: PokeApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedPokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        pokeApiService = retrofit.create(PokeApiService::class.java)

        // Настройка RecyclerView для отображения сохранённых покемонов
        recyclerView = findViewById(R.id.savedPokemonsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SavedPokemonAdapter()
        recyclerView.adapter = adapter

        // Загрузка сохранённых покемонов из базы данных
        loadSavedPokemons()

        // Кнопка для перехода в SearchActivity
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем список при возврате на главный экран
        loadSavedPokemons()
    }

    private fun loadSavedPokemons() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val savedPokemonsList = db.pokemonDao().getAll().first()
            runOnUiThread {
                adapter.submitList(savedPokemonsList)
            }
        }
    }


}
