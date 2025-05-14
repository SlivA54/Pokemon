package com.example.pokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SavedPokemonAdapter(
    private val onItemClick: (SavedPokemon) -> Unit,
    private val onDeleteClick: (SavedPokemon) -> Unit
) : RecyclerView.Adapter<SavedPokemonAdapter.SavedPokemonViewHolder>() {

    private val items = mutableListOf<SavedPokemon>()

    fun submitList(list: List<SavedPokemon>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_pokemon, parent, false)
        return SavedPokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedPokemonViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
        holder.deleteButton.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount(): Int = items.size

    inner class SavedPokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.pokemonName)
        private val typesTextView: TextView = itemView.findViewById(R.id.pokemonTypes)
        private val imageView: ImageView = itemView.findViewById(R.id.pokemonImage)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(pokemon: SavedPokemon) {
            nameTextView.text = pokemon.name.replaceFirstChar { it.uppercase() }
            typesTextView.text = pokemon.types
            Glide.with(itemView.context)
                .load(pokemon.imageUrl)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView)
        }
    }
}
