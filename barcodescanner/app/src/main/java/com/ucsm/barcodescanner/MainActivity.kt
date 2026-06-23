package com.ucsm.barcodescanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ucsm.barcodescanner.databinding.ActivityMainBinding
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.common.InputImage

class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE = 100
    private val CAMERA_PERMISSION_CODE = 200

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón para escanear → abre la galería
        binding.btnEscanear.setOnClickListener {
            abrirGaleria()
        }
    }


    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                // Mostramos la imagen en el ImageView
                binding.imageView.setImageURI(uri)
                // Enviamos la imagen al escáner
                escanearCodigo(uri)
            }
        }
    }

    private fun escanearCodigo(uri: Uri) {

        // 1. Convertimos la URI a Bitmap
        val stream = contentResolver.openInputStream(uri)
        val bitmap: Bitmap = BitmapFactory.decodeStream(stream)

        // 2. Convertimos a InputImage (formato que pide ML Kit)
        val imagen = InputImage.fromBitmap(bitmap, 0)


        val opciones = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39
            ).build()

        val escaner = BarcodeScanning.getClient(opciones)

        // 4. Procesamos la imagen
        escaner.process(imagen)
            .addOnSuccessListener { codigos ->
                // Si encontró al menos un código
                if (codigos.isNotEmpty()) {
                    mostrarResultados(codigos)
                } else {
                    binding.tvResultado.text = "No se detectó ningún código de barras."
                }
            }
            .addOnFailureListener { error ->
                binding.tvResultado.text = "Error: ${error.message}"
            }
    }


    private fun mostrarResultados(codigos: List<Barcode>) {

        val resultado = StringBuilder()
        resultado.appendLine("Códigos encontrados: ${codigos.size}\n")

        for ((indice, codigo) in codigos.withIndex()) {

            resultado.appendLine("── Código ${indice + 1} ──")


            val formato = when (codigo.format) {
                Barcode.FORMAT_QR_CODE  -> "QR Code"
                Barcode.FORMAT_EAN_13   -> "EAN-13"
                Barcode.FORMAT_EAN_8    -> "EAN-8"
                Barcode.FORMAT_CODE_128 -> "Code 128"
                Barcode.FORMAT_CODE_39  -> "Code 39"
                else -> "Desconocido"
            }
            resultado.appendLine("Formato: $formato")


            resultado.appendLine("Valor: ${codigo.rawValue}")

        
            val tipo = when (codigo.valueType) {
                Barcode.TYPE_URL          -> "URL: ${codigo.url?.url}"
                Barcode.TYPE_EMAIL        -> "Email: ${codigo.email?.address}"
                Barcode.TYPE_PHONE        -> "Teléfono: ${codigo.phone?.number}"
                Barcode.TYPE_SMS          -> "SMS: ${codigo.sms?.phoneNumber}"
                Barcode.TYPE_CONTACT_INFO -> "Contacto: ${codigo.contactInfo?.name?.formattedName}"
                Barcode.TYPE_WIFI         -> "WiFi SSID: ${codigo.wifi?.ssid}"
                Barcode.TYPE_TEXT         -> "Texto plano"
                else -> "Tipo genérico"
            }
            resultado.appendLine("Contenido: $tipo")
            resultado.appendLine()
        }

        binding.tvResultado.text = resultado.toString()
    }
}