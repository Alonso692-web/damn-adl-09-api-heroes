package com.example.adl_09

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.adl_09.entities.HeroeEntity
import com.example.adl_09.repositories.HeroeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewHeroeActivity : AppCompatActivity() {

    private val heroeRepository = HeroeRepository()

    lateinit var ivAddHeroe: ImageView
    lateinit var etUrlImagen: EditText
    lateinit var etNombre: EditText
    lateinit var etPoder: EditText
    lateinit var etUniverso: EditText
    lateinit var etDebilidad: EditText
    lateinit var btnGuardar: Button
    lateinit var btnCancelar: Button
    lateinit var btnAgregarImagen: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_heroe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivAddHeroe = findViewById(R.id.ivAddHeroe)
        etUrlImagen = findViewById(R.id.etUrlImagenHeroe)
        etNombre = findViewById(R.id.etNombreHeroe)
        etPoder = findViewById(R.id.etPoderHeroe)
        etUniverso = findViewById(R.id.etUniversoHeroe)
        etDebilidad = findViewById(R.id.etDebilidadHeroe)
        btnGuardar = findViewById(R.id.btnGuardarNuevoHeroe)
        btnCancelar = findViewById(R.id.btnRegresarNuevoHeroe)
        btnAgregarImagen = findViewById(R.id.btnAgregarImagen)

        btnGuardar.setOnClickListener {
            guardarSuperHeroe()
        }

        btnCancelar.setOnClickListener {
            finish()
        }

        btnAgregarImagen.setOnClickListener {
            // Intentar mostrar el url de la imagen que le estamos pasando
            val url = etUrlImagen.text.toString()
            if (url.isNotEmpty()) {
                Glide.with(this).load(url).into(ivAddHeroe)
            } else {
                Toast.makeText(
                    this@NewHeroeActivity,
                    "Ingrese el link de su imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun guardarSuperHeroe() {
        val url = etUrlImagen.text.toString()
        val nombre = etNombre.text.toString()
        val poder = etPoder.text.toString()
        val universo = etUniverso.text.toString()
        val debilidad = etDebilidad.text.toString()

        val heroe: HeroeEntity = HeroeEntity(
            id = 0,
            nombre = nombre,
            poder = poder,
            universo = universo,
            debilidad = debilidad,
            urlImagen = url
        )

        if (url.isNotEmpty() && nombre.isNotEmpty() && poder.isNotEmpty() && universo.isNotEmpty() && debilidad.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val respNuevoHeroe = heroeRepository.createHeroe(heroe)
                    val allHeroes = heroeRepository.getAllHeroes()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@NewHeroeActivity,
                            "Héroe: ${respNuevoHeroe.heroes.nombre} \nCreado exitosamente",
                            Toast.LENGTH_LONG
                        ).show()
                        HeroesActivity.adaptadorRecyler.updateHeroesList(allHeroes.heroes)
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@NewHeroeActivity,
                            "Error ${e.message.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        } else {
            Toast.makeText(this, "Campos vacíos, ingrese toda la información", Toast.LENGTH_SHORT)
                .show()
        }
    }
}