package com.example.pokemon

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

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

        val id = intent.getIntExtra("pokemon_id", -1)
        val name = intent.getStringExtra("pokemon_name") ?: "Unknown"
        val imageUrl = intent.getStringExtra("pokemon_image_url") ?: ""
        val types = intent.getStringExtra("pokemon_types") ?: ""

        nameTextView.text = name
        typesTextView.text = types

        Glide.with(this)
            .load(imageUrl)
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(imageView)

        // Здесь можно добавить загрузку дополнительных данных по id, если нужно
    }
}
