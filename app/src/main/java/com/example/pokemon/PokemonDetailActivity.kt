package com.example.pokemon

import android.os.Bundle
import android.widget.Button
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
    private lateinit var addButton: Button

    private lateinit var pokeApiService: PokeApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        imageView = findViewById(R.id.pokemonImage)
        nameTextView = findViewById(R.id.pokemonName)
        typesTextView = findViewById(R.id.pokemonTypes)
        addButton = findViewById(R.id.btnAdd)

        val id = intent.getIntExtra("pokemon_id", -1)
        val name = intent.getStringExtra("pokemon_name") ?: "Unknown"
        nameTextView.text = name.replaceFirstChar { it.uppercase() }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokeApiService = retrofit.create(PokeApiService::class.java)

        if (id != -1) {
            pokeApiService.getPokemonDetails(id).enqueue(object : Callback<PokemonDetails> {
                override fun onResponse(call: Call<PokemonDetails>, response: Response<PokemonDetails>) {
                    if (response.isSuccessful) {
                        val details = response.body()
                        details?.let {
                            val imageUrl = it.sprites.front_default
                            val typesString = it.types.joinToString(", ") { typeSlot ->
                                typeSlot.type.name.replaceFirstChar { c -> c.uppercase() }
                            }
                            typesTextView.text = typesString

                            Glide.with(this@PokemonDetailActivity)
                                .load(imageUrl)
                                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                                .error(android.R.drawable.stat_notify_error)
                                .into(imageView)
                        }
                    } else {
                        typesTextView.text = "Не удалось загрузить типы"
                    }
                }

                override fun onFailure(call: Call<PokemonDetails>, t: Throwable) {
                    typesTextView.text = "Ошибка загрузки"
                }
            })
        } else {
            typesTextView.text = "ID покемона не указан"
        }

        addButton.setOnClickListener {
            // Здесь добавьте логику сохранения покемона, например в базу данных
            Toast.makeText(this, "$name добавлен", Toast.LENGTH_SHORT).show()
        }
    }
}
