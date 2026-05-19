package com.example.act07

import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val dao: ProductoDao) {

    // Expone el Flow directo del DAO
    val productos: Flow<List<Producto>> = dao.listarTodos()

    suspend fun insertar(producto: Producto): Long {
        return dao.insertar(producto)
    }
    suspend fun buscarPorId(id: Int): Producto? {
        return dao.buscarPorId(id)
    }
    suspend fun actualizar(producto: Producto): Int {
        return dao.actualizar(producto)
    }
    suspend fun eliminar(id: Int): Int {
        return dao.eliminarPorId(id)
    }
    // Validación de negocio antes de guardar
    suspend fun insertarConValidacion(producto: Producto): Result<Long> {
        if (producto.stock < 0)
            return Result.failure(IllegalArgumentException("El stock no puede ser negativo"))
        if (producto.precio <= 0)
            return Result.failure(IllegalArgumentException("El precio debe ser mayor a 0"))
        return Result.success(dao.insertar(producto))
    }
}