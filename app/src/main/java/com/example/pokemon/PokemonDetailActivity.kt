package com.example.pokemon

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var typesTextView: TextView
    private lateinit var addButton: Button

    private lateinit var pokeApiService: PokeApiService

    private var currentPokemonId: Int = -1
    private var currentPokemonName: String = ""
    private var currentImageUrl: String = ""
    private var currentTypesString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        imageView = findViewById(R.id.pokemonImage)
        nameTextView = findViewById(R.id.pokemonName)
        typesTextView = findViewById(R.id.pokemonTypes)
        addButton = findViewById(R.id.btnAdd)

        currentPokemonId = intent.getIntExtra("pokemon_id", -1)
        currentPokemonName = intent.getStringExtra("pokemon_name") ?: "Unknown"
        nameTextView.text = currentPokemonName.replaceFirstChar { it.uppercase() }

        val showAddButton = intent.getBooleanExtra("show_add_button", true)

        // Управляем видимостью кнопки добавления
        addButton.visibility = if (showAddButton) View.VISIBLE else View.GONE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokeApiService = retrofit.create(PokeApiService::class.java)

        if (currentPokemonId != -1) {
            pokeApiService.getPokemonDetails(currentPokemonId).enqueue(object : Callback<PokemonDetails> {
                override fun onResponse(call: Call<PokemonDetails>, response: Response<PokemonDetails>) {
                    if (response.isSuccessful) {
                        val details = response.body()
                        details?.let {
                            currentImageUrl = it.sprites.front_default ?: ""
                            val typesList = it.types.map { typeSlot ->
                                typeSlot.type.name.replaceFirstChar { c -> c.uppercase() }
                            }
                            currentTypesString = typesList.joinToString(", ")

                            typesTextView.text = currentTypesString.ifEmpty { "Типы не указаны" }

                            Glide.with(this@PokemonDetailActivity)
                                .load(currentImageUrl)
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
            if (currentPokemonId == -1) {
                Toast.makeText(this, "Невозможно сохранить: неверный ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pokemonToSave = SavedPokemon(
                id = currentPokemonId,
                name = currentPokemonName,
                imageUrl = currentImageUrl,
                types = currentTypesString
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val db = AppDatabase.getDatabase(this@PokemonDetailActivity)
                    db.pokemonDao().insert(pokemonToSave)
                }
                Toast.makeText(this@PokemonDetailActivity, "$currentPokemonName сохранён", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
