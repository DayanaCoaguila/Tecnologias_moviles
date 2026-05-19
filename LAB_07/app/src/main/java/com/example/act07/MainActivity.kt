package com.example.act07

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.act07.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: ProductoRepository  // ← Repository, no DAO directo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crear Repository pasándole el DAO
        val dao = MarketplaceDatabase.getInstance(this).productoDao()
        repository = ProductoRepository(dao)

        // Observar lista en tiempo real desde el Repository
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.productos.collectLatest { lista ->
                    mostrarProductos(lista)
                }
            }
        }

        binding.btnRegistrar.setOnClickListener { registrar() }
        binding.btnBuscar.setOnClickListener   { buscar()    }
        binding.btnModificar.setOnClickListener { modificar() }
        binding.btnEliminar.setOnClickListener  { eliminar()  }
    }

    private fun mostrarProductos(lista: List<Producto>) {
        val sb = StringBuilder()
        if (lista.isEmpty()) {
            sb.append("No hay productos registrados")
        } else {
            lista.forEach { p ->
                sb.append("ID: ${p.id} | ${p.nombre}\n")
                sb.append("   Precio: S/ ${p.precio} | Stock: ${p.stock}\n")
                sb.append("   Categoría: ${p.categoria}\n\n")
            }
        }
        binding.tvLista.text = sb.toString()
    }

    private fun registrar() {
        val nombre    = binding.txtNombre.text.toString()
        val precio    = binding.txtPrecio.text.toString()
        val stock     = binding.txtStock.text.toString()
        val categoria = binding.txtCategoria.text.toString()

        if (nombre.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
            toast("Complete los campos obligatorios"); return
        }

        val producto = Producto(
            nombre      = nombre,
            descripcion = binding.txtDescripcion.text.toString(),
            precio      = precio.toDouble(),
            stock       = stock.toInt(),
            categoria   = categoria
        )

        lifecycleScope.launch {
            // Usa el Repository con validación de negocio
            val result = repository.insertarConValidacion(producto)
            result.onSuccess { limpiarCampos(); toast("Producto registrado") }
                .onFailure { toast("Error: ${it.message}") }
        }
    }

    private fun buscar() {
        val idStr = binding.txtId.text.toString()
        if (idStr.isEmpty()) { toast("Ingrese un ID"); return }

        lifecycleScope.launch {
            val p = repository.buscarPorId(idStr.toInt())
            if (p != null) {
                binding.txtNombre.setText(p.nombre)
                binding.txtDescripcion.setText(p.descripcion)
                binding.txtPrecio.setText(p.precio.toString())
                binding.txtStock.setText(p.stock.toString())
                binding.txtCategoria.setText(p.categoria)
            } else {
                toast("Producto no encontrado")
            }
        }
    }

    private fun modificar() {
        val idStr = binding.txtId.text.toString()
        if (idStr.isEmpty()) { toast("Ingrese el ID"); return }

        val producto = Producto(
            id          = idStr.toInt(),
            nombre      = binding.txtNombre.text.toString(),
            descripcion = binding.txtDescripcion.text.toString(),
            precio      = binding.txtPrecio.text.toString().toDouble(),
            stock       = binding.txtStock.text.toString().toInt(),
            categoria   = binding.txtCategoria.text.toString()
        )

        lifecycleScope.launch {
            val filas = repository.actualizar(producto)
            if (filas > 0) toast("Producto actualizado") else toast("No existe ese producto")
        }
    }

    private fun eliminar() {
        val idStr = binding.txtId.text.toString()
        if (idStr.isEmpty()) { toast("Ingrese el ID"); return }

        lifecycleScope.launch {
            val filas = repository.eliminar(idStr.toInt())
            limpiarCampos()
            if (filas > 0) toast("Producto eliminado") else toast("No existe ese producto")
        }
    }

    private fun limpiarCampos() {
        binding.txtId.setText("")
        binding.txtNombre.setText("")
        binding.txtDescripcion.setText("")
        binding.txtPrecio.setText("")
        binding.txtStock.setText("")
        binding.txtCategoria.setText("")
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}