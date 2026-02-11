package com.quiroz.mitalla.repository

import com.quiroz.mitalla.model.Talla
import com.quiroz.mitalla.network.RetrofitInstance
import retrofit2.Response

/**
 * Repository para gestionar las operaciones de datos de Talla
 * Separa la lógica de acceso a datos del ViewModel
 */
class TallaRepository {

    private val api = RetrofitInstance.api

    /**
     * Obtener lista de todas las tallas
     */
    suspend fun listar(): List<Talla> {
        return api.listar()
    }

    /**
     * Obtener detalle de una talla por ID
     */
    suspend fun detalle(id: Int): Talla {
        return api.detalle(id)
    }

    /**
     * Crear nueva talla
     */
    suspend fun crear(talla: Talla): Talla {
        return api.crear(talla)
    }

    /**
     * Editar talla existente
     */
    suspend fun editar(id: Int, talla: Talla): Talla {
        return api.editar(id, talla)
    }

    /**
     * Eliminar talla por ID
     */
    suspend fun eliminar(id: Int): Response<Unit> {
        return api.eliminar(id)
    }
}
