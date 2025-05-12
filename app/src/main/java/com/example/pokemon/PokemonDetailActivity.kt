package com.example.pokemon

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var typesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        imageView = findViewById(R.id.pokemonImage)
        nameTextView = findViewById(R.id.pokemonName)
        typesTextView = findViewById(R.id.pokemonTypes)

        val pokemonUrl = intent.getStringExtra("pokemon_url")
        val pokemonName = intent.getStringExtra("pokemon_name")?.replaceFirstChar { it.uppercase() }

        nameTextView.text = pokemonName ?: "Unknown"

        if (pokemonUrl == null) {
            Toast.makeText(this, "URL покемона не передан", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchPokemonDetails(pokemonUrl)
    }

    private fun fetchPokemonDetails(url: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PokeApiService::class.java)

        // Путь в URL вида https://pokeapi.co/api/v2/pokemon/{id}/
        // Нам нужно получить endpoint после baseUrl, например "pokemon/25/"
        val endpoint = url.removePrefix("https://pokeapi.co/api/v2/")

        service.getPokemonDetails(endpoint).enqueue(object : Callback<PokemonDetails> {
            override fun onResponse(call: Call<PokemonDetails>, response: Response<PokemonDetails>) {
                if (response.isSuccessful) {
                    val details = response.body()
                    details?.let {
                        val spriteUrl = it.sprites.front_default
                        Glide.with(this@PokemonDetailActivity)
                            .load(spriteUrl)
                            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                            .error(android.R.drawable.stat_notify_error)
                            .into(imageView)

                        val types = it.types.joinToString(", ") { typeSlot -> typeSlot.type.name.replaceFirstChar { c -> c.uppercase() } }
                        typesTextView.text = "Типы: $types"
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
}
