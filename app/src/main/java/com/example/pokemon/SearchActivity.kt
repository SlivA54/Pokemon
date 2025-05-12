package com.example.pokemon

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchView = findViewById<SearchView>(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter()
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isBlank()) {
                    Toast.makeText(this@SearchActivity, "Введите имя покемона", Toast.LENGTH_SHORT).show()
                    return false
                }
                performSearch(query.trim().lowercase())
                searchView.clearFocus() // скрыть клавиатуру
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Можно реализовать поиск по мере ввода, если нужно
                return false
            }
        })
    }

    private fun performSearch(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PokeApiService::class.java)

        // Получаем полный список покемонов (limit большой, чтобы получить всех)
        service.getPokemonList(1118, 0).enqueue(object : Callback<PokemonListResponse> {
            override fun onResponse(call: Call<PokemonListResponse>, response: Response<PokemonListResponse>) {
                if (response.isSuccessful) {
                    val list = response.body()?.results ?: emptyList()
                    val filtered = list.filter { it.name.contains(query, ignoreCase = true) }
                    if (filtered.isEmpty()) {
                        Toast.makeText(this@SearchActivity, "Покемоны не найдены", Toast.LENGTH_SHORT).show()
                    }
                    adapter.submitList(filtered)
                } else {
                    Toast.makeText(this@SearchActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
