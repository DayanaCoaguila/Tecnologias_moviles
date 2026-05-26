package com.ucsm.laboratorio_08

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import com.google.firebase.database.*

// ─────────────────────────────────────────────────────────────
//  Ejercicio 32 – Mostrar estudiantes en tiempo real
//  Ejercicio 33 – CRUD completo: Leer, Actualizar, Eliminar
//
//  Cada fila del ListView muestra nombre + carrera + curso.
//  Pulsación corta  → diálogo para ACTUALIZAR (UPDATE)
//  Pulsación larga  → diálogo para ELIMINAR   (DELETE)
// ─────────────────────────────────────────────────────────────
class ListaActivity : AppCompatActivity() {

    // ── Vistas ──────────────────────────────────────────────
    private lateinit var listView: ListView
    private lateinit var tvSinDatos: TextView

    // ── Firebase ─────────────────────────────────────────────
    private lateinit var estudiantesRef: DatabaseReference

    // ── Datos en memoria ─────────────────────────────────────
    private val listaEstudiantes = mutableListOf<Estudiante>()
    private lateinit var adapter: ArrayAdapter<String>
    private val textos = mutableListOf<String>()   // texto mostrado en cada fila

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listaRoot)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        estudiantesRef = FirebaseDatabase.getInstance().getReference("Estudiantes")

        listView   = findViewById(R.id.listViewEstudiantes)
        tvSinDatos = findViewById(R.id.tvSinDatos)

        // Adapter simple de texto
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
        listView.adapter = adapter

        // ── Ejercicio 32 – Escuchar cambios en tiempo real ──
        cargarEstudiantesEnTiempoReal()

        // ── Ejercicio 33 – Pulsación corta → Actualizar ─────
        listView.setOnItemClickListener { _, _, position, _ ->
            mostrarDialogoActualizar(listaEstudiantes[position])
        }

        // ── Ejercicio 33 – Pulsación larga → Eliminar ───────
        listView.setOnItemLongClickListener { _, _, position, _ ->
            mostrarDialogoEliminar(listaEstudiantes[position])
            true
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Ejercicio 32 – READ en tiempo real con ValueEventListener
    // ─────────────────────────────────────────────────────────
    private fun cargarEstudiantesEnTiempoReal() {
        estudiantesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaEstudiantes.clear()
                textos.clear()

                for (child in snapshot.children) {
                    val estudiante = child.getValue(Estudiante::class.java)
                    if (estudiante != null) {
                        listaEstudiantes.add(estudiante)
                        textos.add(
                            "👤 ${estudiante.nombre}\n" +
                                    "   Carrera: ${estudiante.carrera}\n" +
                                    "   Curso: ${estudiante.curso}"
                        )
                    }
                }

                adapter.notifyDataSetChanged()

                // Mostrar mensaje si no hay datos
                if (listaEstudiantes.isEmpty()) {
                    tvSinDatos.visibility = View.VISIBLE
                    listView.visibility  = View.GONE
                } else {
                    tvSinDatos.visibility = View.GONE
                    listView.visibility  = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ListaActivity,
                    "Error al cargar: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // ─────────────────────────────────────────────────────────
    //  Ejercicio 33 – UPDATE: diálogo para editar estudiante
    // ─────────────────────────────────────────────────────────
    private fun mostrarDialogoActualizar(estudiante: Estudiante) {
        // Inflar un layout con los campos editables
        val view = layoutInflater.inflate(R.layout.dialog_actualizar, null)
        val etNombre  = view.findViewById<EditText>(R.id.dialogEtNombre)
        val spinCarrera = view.findViewById<Spinner>(R.id.dialogSpinCarrera)
        val spinCurso   = view.findViewById<Spinner>(R.id.dialogSpinCurso)

        // Pre-cargar valores actuales
        etNombre.setText(estudiante.nombre)

        // Seleccionar la carrera actual en el spinner
        val carreras = resources.getStringArray(R.array.carreras)
        val posCarrera = carreras.indexOf(estudiante.carrera)
        if (posCarrera >= 0) spinCarrera.setSelection(posCarrera)

        // Seleccionar el curso actual en el spinner
        val cursos = resources.getStringArray(R.array.cursos)
        val posCurso = cursos.indexOf(estudiante.curso)
        if (posCurso >= 0) spinCurso.setSelection(posCurso)

        AlertDialog.Builder(this)
            .setTitle("✏️ Actualizar Estudiante")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre  = etNombre.text.toString().trim()
                val nuevaCarrera = spinCarrera.selectedItem.toString()
                val nuevoCurso   = spinCurso.selectedItem.toString()

                if (nuevoNombre.isEmpty()) {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // updateChildren modifica solo los campos indicados (UPDATE)
                val cambios = mapOf(
                    "nombre"  to nuevoNombre,
                    "carrera" to nuevaCarrera,
                    "curso"   to nuevoCurso
                )
                estudiantesRef.child(estudiante.estudianteid).updateChildren(cambios)
                    .addOnSuccessListener {
                        Toast.makeText(this, "✅ Estudiante actualizado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // ─────────────────────────────────────────────────────────
    //  Ejercicio 33 – DELETE: confirmar y eliminar estudiante
    // ─────────────────────────────────────────────────────────
    private fun mostrarDialogoEliminar(estudiante: Estudiante) {
        AlertDialog.Builder(this)
            .setTitle("🗑️ Eliminar Estudiante")
            .setMessage("¿Eliminar a \"${estudiante.nombre}\"?\nEsta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ ->
                // removeValue elimina el nodo completo (DELETE)
                estudiantesRef.child(estudiante.estudianteid).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "🗑️ Estudiante eliminado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
