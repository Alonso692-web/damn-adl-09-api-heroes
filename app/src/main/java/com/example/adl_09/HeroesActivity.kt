package com.example.adl_09

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adl_09.entities.HeroeEntity
import com.example.adl_09.repositories.HeroeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeroesActivity : AppCompatActivity() {

    private val heroeRepository = HeroeRepository()

    companion object {
        var adaptadorRecyler: CustomAdapterRecyclerView = CustomAdapterRecyclerView(emptyList())
    }

    // lateinit var adaptadorRecyler: CustomAdapterRecyclerView

    lateinit var btnAgregarHeroe: Button
    lateinit var recyclerViewHeroes: RecyclerView

    lateinit var listaHeroes: List<HeroeEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_heroes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recycler View
        btnAgregarHeroe = findViewById(R.id.btnAgregarHeroe)

        recyclerViewHeroes = findViewById(R.id.rvHeroes)
        recyclerViewHeroes.layoutManager = LinearLayoutManager(this)
        // Le pasamos el adaptador al RecyclerView
        recyclerViewHeroes.adapter = adaptadorRecyler

        obtenerHeroes()

        adaptadorRecyler.setOnItemClickListener(object :
            CustomAdapterRecyclerView.onItemClickListener {
            override fun onItemClick(heroe: HeroeEntity) {
                actualizarHeroe(heroe)
                Log.d("Heroes", "Clic corto, Heroes ${heroe.nombre}}")
            }

            override fun onItemLongClick(heroe: HeroeEntity) {
                Log.d("Heroes", "Clic largo: Heroes ${heroe.nombre}")
                eliminarHeroe(heroe)
            }

        })

        btnAgregarHeroe.setOnClickListener {
            startActivity(Intent(this@HeroesActivity, NewHeroeActivity::class.java))
        }

    }

    override fun onRestart() {
        super.onRestart()
        obtenerHeroes()
    }

    override fun onStart() {
        super.onStart()
        obtenerHeroes()
    }

    private fun actualizarHeroe(heroe: HeroeEntity) {
        val intentUpdateHeroe = Intent(this, UpdateHeroeActivity::class.java)
        intentUpdateHeroe.putExtra("idHeroe", heroe.id.toString())
        intentUpdateHeroe.putExtra("nombreHeroe", heroe.nombre)
        intentUpdateHeroe.putExtra("universoHeroe", heroe.universo)
        intentUpdateHeroe.putExtra("urlImagenHeroe", heroe.urlImagen)
        intentUpdateHeroe.putExtra("poderHeroe", heroe.poder)
        intentUpdateHeroe.putExtra("debilidadHeroe", heroe.debilidad)
        startActivity(intentUpdateHeroe)
    }

    private fun eliminarHeroe(heroe: HeroeEntity) {
        //---------------------------------------------------------------------------------
        val builder = AlertDialog.Builder(this@HeroesActivity).setCancelable(false)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_delete_heroe, null)
        val dialog = builder.setView(dialogView).create()

        dialogView.findViewById<EditText>(R.id.etNombreDelete).setHint(heroe.nombre)

        // Configurar los botones
        dialogView.findViewById<Button>(R.id.btnCancelarDelete).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnAceptarDelete).setOnClickListener {
            val nombreEntrada =
                dialogView.findViewById<EditText>(R.id.etNombreDelete).text.toString()

            // Aquí puedes hacer algo con los datos ingresados
            if (nombreEntrada.equals("")) {
                Toast.makeText(
                    this@HeroesActivity,
                    "Ingrese el nombre por favor",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                if (nombreEntrada != heroe.nombre) {
                    Toast.makeText(
                        this@HeroesActivity,
                        "Nombre incorrecto",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            withContext(Dispatchers.Main) {
                                val heroeEliminar = heroeRepository.deleteHeroe(heroe.id)
                                Toast.makeText(
                                    this@HeroesActivity,
                                    "Héroe: ${heroe.nombre} \nEliminado exitosamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                            }

                            // Obtener la lista actualizada de héroes
                            val allHeroes = heroeRepository.getAllHeroes()

                            // Actualizar el adaptador en el hilo principal
                            withContext(Dispatchers.Main) {
                                Log.d("Cat", "Actualizar el adaptador en el hilo principal")
                                adaptadorRecyler.updateHeroesList(allHeroes.heroes)
                            }

                        } catch (e: Exception) {
                            Log.d("Cat", "Error, cae en el Catch ${e.message.toString()}")
                            if (e is retrofit2.HttpException && e.code() == 404) {
                                // Manejar el 404 como una lista vacía
                                withContext(Dispatchers.Main) {
                                    adaptadorRecyler.updateHeroesList(emptyList())
                                    // adaptadorRecyler.notifyDataSetChanged()
                                }
                            } else {
                                // Manejar otras excepciones como errores
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@HeroesActivity,
                                        "Error al eliminar: $e",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        builder.setView(dialogView)
        // Hacer que el dialog no se pueda cancelar tocando fuera
        builder.setCancelable(false)

        // Establecer el fondo con bordes redondeados (opcional)
        dialog.window?.setBackgroundDrawableResource(android.R.drawable.dialog_holo_light_frame)
        dialog.show()
    }

    private fun obtenerHeroes() {
        // Mandar a segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            // Mandar a srgundo plano
            try {
                // Segundo plano
                val heroes = heroeRepository.getAllHeroes()
                // Mandar a primer plano = UI - Vista
                withContext(Dispatchers.Main) {
                    // Agregar el listado al list view o recycler view o text view
                    adaptadorRecyler.updateHeroesList(heroes.heroes)
                }
            } catch (e: Exception) {
                if (e is retrofit2.HttpException && e.code() == 404) {
                    // Manejar el 404 como una lista vacía
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@HeroesActivity,
                            "Sin héroes aún",
                            Toast.LENGTH_SHORT
                        ).show()
                        //adaptadorRecyler.updateHeroesList(emptyList())
                    }
                } else {
                    // Manejar otras excepciones como errores
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@HeroesActivity,
                            "Sin héroes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                Log.d("Cat", "Error al obtener héroes ${e}")
            }
        }
    }

    /*
    private fun crearHeroe() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val heroe: HeroeEntity = HeroeEntity(
                    id = 0,
                    nombre = "Flash",
                    poder = "Velocidad",
                    universo = "DC",
                    debilidad = "Atrofia muscular",
                    urlImagen = "https://upload.wikimedia.org/wikipedia/en/b/b7/Flash_%28Barry_Allen%29.png"
                )
                val respNuevoHeroe = heroeRepository.createHeroe(heroe)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@HeroesActivity,
                        "Heroe creado ${respNuevoHeroe.msg}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("Heroes", respNuevoHeroe.toString())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@HeroesActivity,
                        "Error ${e.message.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("Heroes", "Error $e")
                }
            }
        }
    }

    private fun obtenerHeroePorID(id: Long = 1) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Segundo plano
                val heroe = heroeRepository.getHeroeById(id)
                // Primer Plano - hilo principal - Vistas - UI
                withContext(Dispatchers.Main) {
                    //tvMain.text = heroe.heroes.nombre
                    val link = heroe.heroes.urlImagen
                    //Glide.with(this@HeroesActivity).load(link).into(imageView);
                    // Glide.with(this@HeroesActivity).load(heroe.heroes.urlImagen).into(imageView);
                    Toast.makeText(
                        this@HeroesActivity,
                        "Heroe: ${heroe.heroes.nombre}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Heroes", "Link: " + heroe.heroes.urlImagen)
                }
            } catch (e: Exception) {
                Log.d("Heroes", "Error $e")
            }
        }

    }
     */

}
