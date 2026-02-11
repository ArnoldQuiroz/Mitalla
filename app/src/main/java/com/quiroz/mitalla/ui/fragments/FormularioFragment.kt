package com.quiroz.mitalla.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.quiroz.mitalla.R
import com.quiroz.mitalla.model.Talla
import com.quiroz.mitalla.viewmodel.TallaViewModel

class FormularioFragment : Fragment() {

    private val viewModel: TallaViewModel by activityViewModels()
    private val args: FormularioFragmentArgs by navArgs()
    
    private lateinit var etNombre: TextInputEditText
    private lateinit var etRelacion: TextInputEditText
    private lateinit var etPrenda: TextInputEditText
    private lateinit var etTalla: TextInputEditText
    private lateinit var etNotas: TextInputEditText
    private lateinit var switchActivo: SwitchMaterial
    private lateinit var btnGuardar: MaterialButton
    private lateinit var progressBar: ProgressBar

    private var esEdicion = false
    private var tallaId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_formulario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        etNombre = view.findViewById(R.id.etNombre)
        etRelacion = view.findViewById(R.id.etRelacion)
        etPrenda = view.findViewById(R.id.etPrenda)
        etTalla = view.findViewById(R.id.etTalla)
        etNotas = view.findViewById(R.id.etNotas)
        switchActivo = view.findViewById(R.id.switchActivo)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        progressBar = view.findViewById(R.id.progressBar)

        // Verificar si es edición o creación
        tallaId = args.tallaId
        esEdicion = tallaId != -1

        if (esEdicion) {
            // Cargar datos para editar
            viewModel.cargarDetalle(tallaId)
            btnGuardar.text = "Actualizar"
        } else {
            btnGuardar.text = "Crear"
        }

        // Botón guardar
        btnGuardar.setOnClickListener {
            guardarTalla()
        }

        // Observar ViewModel
        observarViewModel()
    }

    private fun observarViewModel() {
        // Observar talla actual (para edición)
        viewModel.tallaActual.observe(viewLifecycleOwner) { talla ->
            talla?.let {
                cargarDatosEnFormulario(it)
            }
        }

        // Observar loading
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnGuardar.isEnabled = !isLoading
        }

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.limpiarError()
            }
        }

        // Observar operación exitosa
        viewModel.operacionExitosa.observe(viewLifecycleOwner) { exitosa ->
            if (exitosa) {
                val mensaje = if (esEdicion) "Talla actualizada" else "Talla creada"
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                viewModel.resetearOperacion()
                viewModel.limpiarTallaActual()
                // Volver a la lista
                findNavController().navigateUp()
            }
        }
    }

    private fun cargarDatosEnFormulario(talla: Talla) {
        etNombre.setText(talla.nombre)
        etRelacion.setText(talla.relacion)
        etPrenda.setText(talla.prenda)
        etTalla.setText(talla.talla)
        etNotas.setText(talla.notas ?: "")
        switchActivo.isChecked = talla.activo
    }

    private fun guardarTalla() {
        // Validar campos
        val nombre = etNombre.text.toString().trim()
        val relacion = etRelacion.text.toString().trim()
        val prenda = etPrenda.text.toString().trim()
        val talla = etTalla.text.toString().trim()
        val notas = etNotas.text.toString().trim()
        val activo = switchActivo.isChecked

        if (nombre.isEmpty()) {
            etNombre.error = "El nombre es obligatorio"
            etNombre.requestFocus()
            return
        }

        if (relacion.isEmpty()) {
            etRelacion.error = "La relación es obligatoria"
            etRelacion.requestFocus()
            return
        }

        if (prenda.isEmpty()) {
            etPrenda.error = "La prenda es obligatoria"
            etPrenda.requestFocus()
            return
        }

        if (talla.isEmpty()) {
            etTalla.error = "La talla es obligatoria"
            etTalla.requestFocus()
            return
        }

        // Crear objeto Talla
        val tallaNueva = Talla(
            id = if (esEdicion) tallaId else null,
            nombre = nombre,
            relacion = relacion,
            prenda = prenda,
            talla = talla,
            notas = notas.ifEmpty { null },
            activo = activo
        )

        // Llamar a ViewModel
        if (esEdicion) {
            viewModel.editarTalla(tallaId, tallaNueva)
        } else {
            viewModel.crearTalla(tallaNueva)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.limpiarTallaActual()
    }
}
