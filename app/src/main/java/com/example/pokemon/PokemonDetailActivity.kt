package com.example.pokemon

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var typesTextView: TextView
    private lateinit var btnAdd: Button

    private var currentPokemonId: Int = -1
    private var currentPokemonName: String = ""
    private var currentPokemonImageUrl: String = ""
    private var currentPokemonTypes: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        imageView = findViewById(R.id.pokemonImage)
        nameTextView = findViewById(R.id.pokemonName)
        typesTextView = findViewById(R.id.pokemonTypes)
        btnAdd = findViewById(R.id.btnAdd)

        val pokemonUrl = intent.getStringExtra("pokemon_url")
        currentPokemonName = intent.getStringExtra("pokemon_name")?.replaceFirstChar { it.uppercase() } ?: "Unknown"
        nameTextView.text = currentPokemonName

        if (pokemonUrl == null) {
            Toast.makeText(this, "URL покемона не передан", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchPokemonDetails(pokemonUrl)

        btnAdd.setOnClickListener {
            if (currentPokemonId != -1) {
                savePokemonToDb()
            } else {
                Toast.makeText(this, "Данные покемона не загружены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchPokemonDetails(url: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PokeApiService::class.java)

        val endpoint = url.removePrefix("https://pokeapi.co/api/v2/")

        service.getPokemonDetails(endpoint).enqueue(object : Callback<PokemonDetails> {
            override fun onResponse(call: Call<PokemonDetails>, response: Response<PokemonDetails>) {
                if (response.isSuccessful) {
                    val details = response.body()
                    details?.let {
                        currentPokemonId = extractIdFromUrl(url)
                        currentPokemonImageUrl = it.sprites.front_default ?: ""
                        currentPokemonTypes = it.types.joinToString(", ") { typeSlot ->
                            typeSlot.type.name.replaceFirstChar { c -> c.uppercase() }
                        }

                        typesTextView.text = "Типы: $currentPokemonTypes"

                        Glide.with(this@PokemonDetailActivity)
                            .load(currentPokemonImageUrl)
                            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                            .error(android.R.drawable.stat_notify_error)
                            .into(imageView)
                    }
                } else {
                    Toast.makeText(this@PokemonDetailActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonDetails>, t: Throwable) {
                Toast.makeText(this@PokemonDetailActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun extractIdFromUrl(url: String): Int {
        return url.trimEnd('/').split("/").last().toIntOrNull() ?: -1
    }

    private fun savePokemonToDb() {
        val db = AppDatabase.getDatabase(this)
        val pokemon = SavedPokemon(
            id = currentPokemonId,
            name = currentPokemonName,
            imageUrl = currentPokemonImageUrl,
            types = currentPokemonTypes
        )
        lifecycleScope.launch {
            db.pokemonDao().insert(pokemon)
            runOnUiThread {
                Toast.makeText(this@PokemonDetailActivity, "Покемон добавлен", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
