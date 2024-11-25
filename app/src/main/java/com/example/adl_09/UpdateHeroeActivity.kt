package com.example.adl_09

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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

class UpdateHeroeActivity : AppCompatActivity() {

    private val heroeRepository = HeroeRepository()

    lateinit var btnActualizarHeroe: Button
    lateinit var btnRegresarActualizarHeroe: Button
    lateinit var btnAgregarImagenUpdate: ImageButton
    lateinit var ivImagenUpdate: ImageView
    lateinit var etNombreHeroeUpdate: EditText
    lateinit var etPoderesHeroeUpdate: EditText
    lateinit var etUniversoHeroeUpdate: EditText
    lateinit var etUrlHeroeUpdate: EditText
    lateinit var etDebilidadHeroeUdapte: EditText

    // Extra del Intent
    var idHeroExtra: Long = 0
    lateinit var nombreHeroeExtra: String
    lateinit var poderHeroeExtra: String
    lateinit var urlHeroeExtra: String
    lateinit var universoHeroeExtra: String
    lateinit var debilidadHeroeExtra: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_heroe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnActualizarHeroe = findViewById(R.id.btnUpdateHeroe)
        btnRegresarActualizarHeroe = findViewById(R.id.btnRegresarUpdateHeroe)
        ivImagenUpdate = findViewById(R.id.ivUpdateHeroe)
        etNombreHeroeUpdate = findViewById(R.id.etNombreHeroeUpdate)
        etPoderesHeroeUpdate = findViewById(R.id.etPoderHeroeUpdate)
        etUrlHeroeUpdate = findViewById(R.id.etUrlImagenHeroeUpdate)
        etUniversoHeroeUpdate = findViewById(R.id.etUniversoHeroeUpdate)
        etDebilidadHeroeUdapte = findViewById(R.id.etDebilidadHeroeUpdate)
        btnAgregarImagenUpdate = findViewById(R.id.btnAgregarImagenUpdate)

        with(intent) {
            idHeroExtra = getStringExtra("idHeroe").toString().toLong()
            nombreHeroeExtra = getStringExtra("nombreHeroe").toString()
            poderHeroeExtra = getStringExtra("poderHeroe").toString()
            urlHeroeExtra = getStringExtra("urlImagenHeroe").toString()
            universoHeroeExtra = getStringExtra("universoHeroe").toString()
            debilidadHeroeExtra = getStringExtra("debilidadHeroe").toString()
        }

        etNombreHeroeUpdate.setText(nombreHeroeExtra)
        etUniversoHeroeUpdate.setText(universoHeroeExtra)
        etDebilidadHeroeUdapte.setText(debilidadHeroeExtra)
        etUrlHeroeUpdate.setText(urlHeroeExtra)
        etPoderesHeroeUpdate.setText(poderHeroeExtra)
        Glide.with(this).load(urlHeroeExtra).into(ivImagenUpdate)

        btnAgregarImagenUpdate.setOnClickListener {
            val url = etUrlHeroeUpdate.text.toString()
            if (url.isNotEmpty()) {
                Glide.with(this).load(url).into(ivImagenUpdate)
            } else {
                Toast.makeText(
                    this@UpdateHeroeActivity,
                    "Ingrese el link de su imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnRegresarActualizarHeroe.setOnClickListener {
            finish()
        }

        btnActualizarHeroe.setOnClickListener {
            actualizar()
        }


    }

    fun actualizar() {
        if (etUrlHeroeUpdate.text.isNotEmpty() && etNombreHeroeUpdate.text.isNotEmpty() && etUniversoHeroeUpdate.text.isNotEmpty() && etDebilidadHeroeUdapte.text.isNotEmpty() && etPoderesHeroeUpdate.text.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val heroeUpdate = HeroeEntity(
                        id = idHeroExtra,
                        nombre = etNombreHeroeUpdate.text.toString(),
                        urlImagen = etUrlHeroeUpdate.text.toString(),
                        poder = etPoderesHeroeUpdate.text.toString(),
                        debilidad = etDebilidadHeroeUdapte.text.toString(),
                        universo = etUniversoHeroeUpdate.text.toString()
                    )
                    val updateHeroe = heroeRepository.updateHeroe(idHeroExtra, heroeUpdate)
                    val allHeroes = heroeRepository.getAllHeroes()
                    withContext(Dispatchers.Main) {
                        HeroesActivity.adaptadorRecyler.updateHeroesList(allHeroes.heroes)
                        Toast.makeText(
                            this@UpdateHeroeActivity,
                            "Héroe: ${heroeUpdate.nombre} \nActualizado exitosamente",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@UpdateHeroeActivity,
                            "Ha habido un error",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    }
                    Log.d("Heroes", "Error ${e.message}}")
                }
            }
        } else {
            Toast.makeText(
                this@UpdateHeroeActivity,
                "Complete todos los campos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}