package com.example.proyecto3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConversionMonedaActivity extends AppCompatActivity {
    EditText editTextCantidad;
    TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_moneda);

        editTextCantidad = findViewById(R.id.editTextCantidad);
        textResultado = findViewById(R.id.textResultado);
    }

    public void convertirMoneda(View view) {
        String valor = editTextCantidad.getText().toString();
        if (!valor.isEmpty()) {
            double cantidad = Double.parseDouble(valor);
            double resultado = cantidad * 3.7; // Tipo de cambio ficticio
            textResultado.setText("S/. " + resultado);
        } else {
            textResultado.setText("Ingrese una cantidad válida");
        }
    }

    public void volverAMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Opcional: cierra esta pantalla
    }
}

