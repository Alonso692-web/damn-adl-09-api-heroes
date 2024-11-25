package com.example.adl_09

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adl_09.entities.HeroeEntity


class CustomAdapterRecyclerView(var listadoHeroes: List<HeroeEntity>) :
    RecyclerView.Adapter<CustomAdapterRecyclerView.ViewHolder>() {

    /**
     * Toodo es interno al adaptador
     */

    // Despues inicializamos la variable
    private lateinit var miListener: onItemClickListener

    interface onItemClickListener {
        // Despues implementamos lo que hacen los metodos
        fun onItemClick(heroe: HeroeEntity)
        fun onItemLongClick(heroe: HeroeEntity)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        miListener = listener
    }

    // Internamente requerimos una clase
    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        // Referencia a los elementos de mi diseño
        val imagenHeroe: ImageView = itemView.findViewById(R.id.ivHeroeRecyclerView)
        var nombreHeroe: TextView = itemView.findViewById(R.id.tvNameHeroeRecyclerView)
        val superpoderHeroe: TextView = itemView.findViewById(R.id.tvPoderesHeroeRecyclerView)
        val universoHeroe: TextView = itemView.findViewById(R.id.tvUniversoHeroeRecyclerView)
        val debilidadHeroe: TextView = itemView.findViewById(R.id.tvDebilidadHeroeRecyclerView)

        // Confirmar que se crearon las vistas
        init {
            itemView.setOnClickListener {
                miListener.onItemClick(listadoHeroes[adapterPosition])
            }
            itemView.setOnLongClickListener {
                miListener.onItemLongClick(listadoHeroes[adapterPosition])
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crear la lista -  Inflate
        val vista: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_recycler_view, parent, false)
        return ViewHolder(vista, miListener)
    }

    override fun getItemCount() = listadoHeroes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind - Unir los datos con la vista
        val heroe = listadoHeroes[position]
        Glide.with(holder.itemView.context).load(heroe.urlImagen).into(holder.imagenHeroe)
        holder.nombreHeroe.text = heroe.nombre
        holder.universoHeroe.text = heroe.universo
        holder.superpoderHeroe.text = heroe.poder
        holder.debilidadHeroe.text = heroe.debilidad
    }

    fun updateHeroesList(newHeroesList: List<HeroeEntity>) {
        listadoHeroes = newHeroesList
        notifyDataSetChanged()
    }
}