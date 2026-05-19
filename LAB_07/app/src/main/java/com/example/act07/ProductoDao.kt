package com.example.act07

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(producto: Producto): Long

    @Update
    suspend fun actualizar(producto: Producto): Int

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun eliminarPorId(id: Int): Int

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Producto?

    // Sin suspend, retorna Flow para observar cambios en tiempo real
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun listarTodos(): Flow<List<Producto>>
}