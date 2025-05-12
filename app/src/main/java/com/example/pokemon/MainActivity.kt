package com.example.pokemon


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var pokeApiService: PokeApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokeApiService = retrofit.create(PokeApiService::class.java)

        val pokemonName = "pikachu" // Можно заменить на любой другой покемон

        pokeApiService.getPokemon(pokemonName).enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    pokemon?.let {
                        findViewById<TextView>(R.id.pokemonName).text = it.name
                        findViewById<TextView>(R.id.pokemonHeight).text = "Height: ${it.height}"
                        findViewById<TextView>(R.id.pokemonWeight).text = "Weight: ${it.weight}"
                        val imageView = findViewById<ImageView>(R.id.pokemonImage)
                        Picasso.get().load(it.sprites.front_default).into(imageView)
                    }
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
