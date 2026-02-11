package com.quiroz.mitalla.model

data class Talla(
    val id: Int? = null,
    val nombre: String,
    val relacion: String,
    val prenda: String,
    val talla: String,
    val notas: String?,
    val activo: Boolean
)
