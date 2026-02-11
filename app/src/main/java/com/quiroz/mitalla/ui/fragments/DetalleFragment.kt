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
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.quiroz.mitalla.R
import com.quiroz.mitalla.model.Talla
import com.quiroz.mitalla.viewmodel.TallaViewModel

class DetalleFragment : Fragment() {

    private val viewModel: TallaViewModel by activityViewModels()
    private val args: DetalleFragmentArgs by navArgs()
    
    private lateinit var tvNombre: TextView
    private lateinit var tvRelacion: TextView
    private lateinit var tvPrenda: TextView
    private lateinit var tvTalla: TextView
    private lateinit var tvNotas: TextView
    private lateinit var tvActivo: TextView
    private lateinit var btnEditar: MaterialButton
    private lateinit var btnEliminar: MaterialButton
    private lateinit var progressBar: ProgressBar

    private var tallaActual: Talla? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        tvNombre = view.findViewById(R.id.tvNombre)
        tvRelacion = view.findViewById(R.id.tvRelacion)
        tvPrenda = view.findViewById(R.id.tvPrenda)
        tvTalla = view.findViewById(R.id.tvTalla)
        tvNotas = view.findViewById(R.id.tvNotas)
        tvActivo = view.findViewById(R.id.tvActivo)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnEliminar = view.findViewById(R.id.btnEliminar)
        progressBar = view.findViewById(R.id.progressBar)

        // Botón editar
        btnEditar.setOnClickListener {
            tallaActual?.let { talla ->
                val action = DetalleFragmentDirections
                    .actionDetalleToFormulario(talla.id ?: -1)
                findNavController().navigate(action)
            }
        }

        // Botón eliminar
        btnEliminar.setOnClickListener {
            mostrarDialogoEliminar()
        }

        // Observar ViewModel
        observarViewModel()

        // Cargar detalle
        val tallaId = args.tallaId
        viewModel.cargarDetalle(tallaId)
    }

    private fun observarViewModel() {
        // Observar talla actual
        viewModel.tallaActual.observe(viewLifecycleOwner) { talla ->
            talla?.let {
                tallaActual = it
                mostrarDatos(it)
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

        // Observar operación exitosa (eliminación)
        viewModel.operacionExitosa.observe(viewLifecycleOwner) { exitosa ->
            if (exitosa) {
                Toast.makeText(requireContext(), "Talla eliminada", Toast.LENGTH_SHORT).show()
                viewModel.resetearOperacion()
                viewModel.limpiarTallaActual()
                // Volver a la lista
                findNavController().navigateUp()
            }
        }
    }

    private fun mostrarDatos(talla: Talla) {
        tvNombre.text = talla.nombre
        tvRelacion.text = talla.relacion
        tvPrenda.text = talla.prenda
        tvTalla.text = talla.talla
        tvNotas.text = talla.notas ?: "Sin notas"
        
        if (talla.activo) {
            tvActivo.text = "Activo"
            tvActivo.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
        } else {
            tvActivo.text = "Inactivo"
            tvActivo.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
        }
    }

    private fun mostrarDialogoEliminar() {
        tallaActual?.let { talla ->
            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar talla")
                .setMessage("¿Estás seguro de eliminar la talla de ${talla.nombre}?")
                .setPositiveButton("Eliminar") { _, _ ->
                    viewModel.eliminarTalla(talla.id ?: -1)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.limpiarTallaActual()
    }
}
