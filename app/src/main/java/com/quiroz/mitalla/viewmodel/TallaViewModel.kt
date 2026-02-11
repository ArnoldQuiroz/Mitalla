package com.quiroz.mitalla.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiroz.mitalla.model.Talla
import com.quiroz.mitalla.repository.TallaRepository
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la UI
 * Usa Repository para acceder a los datos
 */
class TallaViewModel : ViewModel() {

    // Repository para acceso a datos
    private val repository = TallaRepository()

    // LiveData privados (mutables)
    private val _tallas = MutableLiveData<List<Talla>>()
    val tallas: LiveData<List<Talla>> = _tallas

    private val _tallaActual = MutableLiveData<Talla?>()
    val tallaActual: LiveData<Talla?> = _tallaActual

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> = _operacionExitosa

    /**
     * Cargar todas las tallas desde el servidor
     */
    fun cargarTallas() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val lista = repository.listar()
                _tallas.value = lista
            } catch (e: Exception) {
                _error.value = "Error al cargar tallas: ${e.message}"
                _tallas.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Cargar detalle de una talla por ID
     */
    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val talla = repository.detalle(id)
                _tallaActual.value = talla
            } catch (e: Exception) {
                _error.value = "Error al cargar detalle: ${e.message}"
                _tallaActual.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Crear nueva talla
     */
    fun crearTalla(talla: Talla) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            _operacionExitosa.value = false
            try {
                repository.crear(talla)
                _operacionExitosa.value = true
            } catch (e: Exception) {
                _error.value = "Error al crear talla: ${e.message}"
                _operacionExitosa.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Editar talla existente
     */
    fun editarTalla(id: Int, talla: Talla) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            _operacionExitosa.value = false
            try {
                repository.editar(id, talla)
                _operacionExitosa.value = true
            } catch (e: Exception) {
                _error.value = "Error al editar talla: ${e.message}"
                _operacionExitosa.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Eliminar talla por ID
     */
    fun eliminarTalla(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            _operacionExitosa.value = false
            try {
                val response = repository.eliminar(id)
                if (response.isSuccessful) {
                    _operacionExitosa.value = true
                } else {
                    _error.value = "Error al eliminar talla"
                    _operacionExitosa.value = false
                }
            } catch (e: Exception) {
                _error.value = "Error al eliminar talla: ${e.message}"
                _operacionExitosa.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Limpiar talla actual
     */
    fun limpiarTallaActual() {
        _tallaActual.value = null
    }

    /**
     * Limpiar mensaje de error
     */
    fun limpiarError() {
        _error.value = null
    }

    /**
     * Resetear estado de operación exitosa
     */
    fun resetearOperacion() {
        _operacionExitosa.value = false
    }
}
