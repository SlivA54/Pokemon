package com.example.pokemon

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ResultsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private val allPokemonList = mutableListOf<PokemonListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter()
        recyclerView.adapter = adapter

        val query = intent.getStringExtra("query")?.lowercase() ?: ""

        fetchAllPokemon(query)
    }

    private fun fetchAllPokemon(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PokeApiService::class.java)

        // Получаем полный список покемонов (по API limit=1118 - все покемоны)
        service.getPokemonList(1118, 0).enqueue(object : Callback<PokemonListResponse> {
            override fun onResponse(call: Call<PokemonListResponse>, response: Response<PokemonListResponse>) {
                if (response.isSuccessful) {
                    val list = response.body()?.results ?: emptyList()
                    // Фильтруем по названию
                    val filtered = list.filter { it.name.contains(query, ignoreCase = true) }
                    if (filtered.isEmpty()) {
                        Toast.makeText(this@ResultsActivity, "Покемоны не найдены", Toast.LENGTH_SHORT).show()
                    }
                    adapter.submitList(filtered)
                } else {
                    Toast.makeText(this@ResultsActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                Toast.makeText(this@ResultsActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
