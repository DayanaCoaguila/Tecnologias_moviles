package com.example.catalogoactivity.model

data class Producto(
    val nombre: String,
    val cantidad: Int,
    val precio: Double,
    val imagen: Int   // 👈 ESTO
)