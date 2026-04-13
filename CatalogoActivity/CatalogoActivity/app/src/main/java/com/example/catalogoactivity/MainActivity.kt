package com.example.catalogoactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoactivity.adapter.ProductoAdapter
import com.example.catalogoactivity.model.Producto


class CatalogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerProductos)
        val lista = listOf(
            Producto("Laptop", 2, 2500.0, R.drawable.laptop ),
            Producto("Mouse", 5, 50.0,  R.drawable.mouse),
            Producto("Teclado", 3, 120.0, R.drawable.teclado)
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProductoAdapter(lista)
    }


}