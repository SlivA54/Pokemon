package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchView = findViewById<SearchView>(R.id.searchView)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        btnSearch.setOnClickListener {
            val query = searchView.query.toString().trim()
            if (query.isEmpty()) {
                Toast.makeText(this, "Введите имя покемона для поиска", Toast.LENGTH_SHORT).show()
            } else {
                // Передаём запрос в ResultsActivity
                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("query", query)
                startActivity(intent)
            }
        }
    }
}
