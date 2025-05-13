package com.example.pokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SavedPokemonAdapter(
    private val onItemClick: (SavedPokemon) -> Unit
) : RecyclerView.Adapter<SavedPokemonAdapter.ViewHolder>() {

    private val items = mutableListOf<SavedPokemon>()

    fun submitList(list: List<SavedPokemon>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.pokemonImage)
        val nameTextView: TextView = view.findViewById(R.id.pokemonName)
        val typesTextView: TextView = view.findViewById(R.id.pokemonTypes)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = items[position]
        holder.nameTextView.text = pokemon.name
        holder.typesTextView.text = pokemon.types
        Glide.with(holder.itemView.context)
            .load(pokemon.imageUrl)
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = items.size
}
