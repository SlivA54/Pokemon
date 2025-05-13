package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private lateinit var typeSpinner: Spinner
    private lateinit var searchView: SearchView

    private lateinit var pokeApiService: PokeApiService
    private val allTypes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchView = findViewById(R.id.searchView)
        typeSpinner = findViewById(R.id.typeSpinner)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PokemonAdapter { pokemonItem ->
            val id = pokemonItem.url.trimEnd('/').split("/").last()
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

            // В этом примере у PokemonListItem нет типов, если есть - добавьте логику получения и передачи типов
            val typesString = "Типы не указаны" // или получите из другой модели / запроса

            val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                putExtra("pokemon_id", id.toInt())
                putExtra("pokemon_name", pokemonItem.name.replaceFirstChar { it.uppercase() })
                putExtra("pokemon_image_url", imageUrl)
                putExtra("pokemon_types", typesString)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokeApiService = retrofit.create(PokeApiService::class.java)

        loadTypes()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query.trim().lowercase())
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?) = false
        })

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val currentQuery = searchView.query.toString().trim().lowercase()
                performSearch(currentQuery)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadTypes() {
        pokeApiService.getTypeList().enqueue(object : Callback<TypeListResponse> {
            override fun onResponse(call: Call<TypeListResponse>, response: Response<TypeListResponse>) {
                if (response.isSuccessful) {
                    allTypes.clear()
                    allTypes.add("Все типы") // опция без фильтра по типу
                    allTypes.addAll(response.body()?.results?.map { it.name } ?: emptyList())
                    val adapterSpinner = ArrayAdapter(this@SearchActivity, android.R.layout.simple_spinner_item, allTypes)
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    typeSpinner.adapter = adapterSpinner
                }
            }
            override fun onFailure(call: Call<TypeListResponse>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Ошибка загрузки типов", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun performSearch(query: String) {
        val selectedType = typeSpinner.selectedItem as String

        if (selectedType == "Все типы") {
            pokeApiService.getPokemonList(1118, 0).enqueue(object : Callback<PokemonListResponse> {
                override fun onResponse(call: Call<PokemonListResponse>, response: Response<PokemonListResponse>) {
                    if (response.isSuccessful) {
                        val list = response.body()?.results ?: emptyList()
                        val filtered = list.filter { it.name.contains(query, ignoreCase = true) }
                        adapter.submitList(filtered)
                        if (filtered.isEmpty()) Toast.makeText(this@SearchActivity, "Покемоны не найдены", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                    Toast.makeText(this@SearchActivity, "Ошибка загрузки покемонов", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            pokeApiService.getPokemonByType(selectedType).enqueue(object : Callback<TypeDetailResponse> {
                override fun onResponse(call: Call<TypeDetailResponse>, response: Response<TypeDetailResponse>) {
                    if (response.isSuccessful) {
                        val pokemonsByType = response.body()?.pokemon?.map { it.pokemon } ?: emptyList()
                        val filtered = pokemonsByType.filter { it.name.contains(query, ignoreCase = true) }
                        adapter.submitList(filtered)
                        if (filtered.isEmpty()) Toast.makeText(this@SearchActivity, "Покемоны не найдены", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<TypeDetailResponse>, t: Throwable) {
                    Toast.makeText(this@SearchActivity, "Ошибка загрузки покемонов по типу", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
