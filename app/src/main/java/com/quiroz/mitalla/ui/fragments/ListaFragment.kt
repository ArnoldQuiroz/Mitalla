package com.quiroz.mitalla.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.quiroz.mitalla.R
import com.quiroz.mitalla.adapter.TallaAdapter
import com.quiroz.mitalla.viewmodel.TallaViewModel

class ListaFragment : Fragment() {

    private val viewModel: TallaViewModel by activityViewModels()
    private lateinit var adapter: TallaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var fabAgregar: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        recyclerView = view.findViewById(R.id.recyclerViewTallas)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        fabAgregar = view.findViewById(R.id.fabAgregarTalla)

        // Configurar Adapter
        adapter = TallaAdapter(
            tallas = emptyList(),
            onItemClick = { talla ->
                // Navegar a detalle
                val action = ListaFragmentDirections
                    .actionListaToDetalle(talla.id ?: -1)
                findNavController().navigate(action)
            },
            onItemLongClick = { talla ->
                // Confirmar eliminación
                mostrarDialogoEliminar(talla.id ?: -1, talla.nombre)
            }
        )

        recyclerView.adapter = adapter

        // FAB para agregar
        fabAgregar.setOnClickListener {
            val action = ListaFragmentDirections
                .actionListaToFormulario(-1)
            findNavController().navigate(action)
        }

        // Observar LiveData
        observarViewModel()

        // Cargar tallas
        viewModel.cargarTallas()
    }

    private fun observarViewModel() {
        // Observar lista de tallas
        viewModel.tallas.observe(viewLifecycleOwner) { tallas ->
            adapter.actualizarLista(tallas)
            
            // Mostrar mensaje si está vacío
            if (tallas.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        // Observar loading
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.limpiarError()
            }
        }

        // Observar si una operación fue exitosa (para recargar)
        viewModel.operacionExitosa.observe(viewLifecycleOwner) { exitosa ->
            if (exitosa) {
                viewModel.cargarTallas()
                viewModel.resetearOperacion()
            }
        }
    }

    private fun mostrarDialogoEliminar(id: Int, nombre: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar talla")
            .setMessage("¿Estás seguro de eliminar la talla de $nombre?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarTalla(id)
                Toast.makeText(requireContext(), "Talla eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        // Recargar al volver a la pantalla
        viewModel.cargarTallas()
    }
}
