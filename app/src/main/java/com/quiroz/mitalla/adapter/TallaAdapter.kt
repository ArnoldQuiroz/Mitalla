package com.quiroz.mitalla.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quiroz.mitalla.R
import com.quiroz.mitalla.model.Talla

class TallaAdapter(
    private var tallas: List<Talla>,
    private val onItemClick: (Talla) -> Unit,
    private val onItemLongClick: (Talla) -> Unit
) : RecyclerView.Adapter<TallaAdapter.TallaViewHolder>() {

    class TallaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvPrenda: TextView = itemView.findViewById(R.id.tvPrenda)
        val tvTalla: TextView = itemView.findViewById(R.id.tvTalla)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_talla, parent, false)
        return TallaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TallaViewHolder, position: Int) {
        val talla = tallas[position]
        
        holder.tvNombre.text = talla.nombre
        holder.tvPrenda.text = talla.prenda
        holder.tvTalla.text = talla.talla

        // Click normal para ver detalle
        holder.itemView.setOnClickListener {
            onItemClick(talla)
        }

        // Long click para eliminar
        holder.itemView.setOnLongClickListener {
            onItemLongClick(talla)
            true
        }
    }

    override fun getItemCount(): Int = tallas.size

    // Actualizar lista de tallas
    fun actualizarLista(nuevaLista: List<Talla>) {
        tallas = nuevaLista
        notifyDataSetChanged()
    }
}
