package com.example.adl_09

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adl_09.entities.HeroeEntity
import com.example.adl_09.repositories.HeroeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val btnHeroe = findViewById<Button>(R.id.btnHeroes)
        val btnCreditos = findViewById<Button>(R.id.btnCreditos)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        btnHeroe.setOnClickListener {
            startActivity(Intent(this, HeroesActivity::class.java))
        }

        btnSalir.setOnClickListener {
            finish()
        }

        btnCreditos.setOnClickListener {
            startActivity(Intent(this, CreditosActivity::class.java))
        }
    }
}