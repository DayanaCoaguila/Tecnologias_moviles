package com.ucsm.laboratorio_08


import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    // ── Vistas ──────────────────────────────────────────────
    private lateinit var etNombre: EditText
    private lateinit var spinCarrera: Spinner
    private lateinit var spinCurso: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnVerLista: Button
    private lateinit var tvContador: TextView

    // ── Firebase ─────────────────────────────────────────────
    private lateinit var estudiantesRef: DatabaseReference
    private var totalEstudiantes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajuste de márgenes para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // ── Referencia al nodo "Estudiantes" en Realtime Database
        estudiantesRef = FirebaseDatabase.getInstance().getReference("Estudiantes")

        // ── Vincular vistas ──────────────────────────────────
        etNombre    = findViewById(R.id.etNombre)
        spinCarrera = findViewById(R.id.spinCarrera)
        spinCurso   = findViewById(R.id.spinCurso)
        btnGuardar  = findViewById(R.id.btnGuardar)
        btnVerLista = findViewById(R.id.btnVerLista)
        tvContador  = findViewById(R.id.tvContador)

        escucharCambiosEnTiempoReal()

        // ── Ejercicio 31: botón Guardar ──────────────────────
        btnGuardar.setOnClickListener { registrarEstudiante() }

        // ── Navegar a la lista ───────────────────────────────
        btnVerLista.setOnClickListener {
            startActivity(Intent(this, ListaActivity::class.java))
        }
    }


    private fun registrarEstudiante() {
        val nombre  = etNombre.text.toString().trim()
        val carrera = spinCarrera.selectedItem.toString()
        val curso   = spinCurso.selectedItem.toString()

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Escribe el nombre del estudiante", Toast.LENGTH_SHORT).show()
            return
        }

        // push() genera una clave única para el nuevo registro
        val id = estudiantesRef.push().key ?: return

        val estudiante = Estudiante(
            estudianteid = id,
            nombre       = nombre,
            carrera      = carrera,
            curso        = curso
        )

        estudiantesRef.child(id).setValue(estudiante)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ Estudiante registrado", Toast.LENGTH_SHORT).show()
                etNombre.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

        private fun escucharCambiosEnTiempoReal() {
        estudiantesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                totalEstudiantes = snapshot.childrenCount.toInt()
                tvContador.text = "Estudiantes registrados: $totalEstudiantes"
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error al leer: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
