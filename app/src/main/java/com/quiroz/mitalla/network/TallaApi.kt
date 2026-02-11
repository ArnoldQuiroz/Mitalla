package com.quiroz.mitalla.network

import com.quiroz.mitalla.model.Talla
import retrofit2.Response
import retrofit2.http.*

interface TallaApi {

    @GET("tallas")
    suspend fun listar(): List<Talla>

    @GET("tallas/{id}")
    suspend fun detalle(@Path("id") id: Int): Talla

    @POST("tallas")
    suspend fun crear(@Body t: Talla): Talla

    @PUT("tallas/{id}")
    suspend fun editar(@Path("id") id: Int, @Body t: Talla): Talla

    @DELETE("tallas/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>
}
