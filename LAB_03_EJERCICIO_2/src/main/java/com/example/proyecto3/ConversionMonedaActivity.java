package com.example.proyecto3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConversionMonedaActivity extends AppCompatActivity {
    EditText editTextCantidad;
    TextView textResultado;
    ProgressBar progressBar;
    TextView textCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_moneda);

        editTextCantidad = findViewById(R.id.editTextCantidad);
        textResultado = findViewById(R.id.textResultado);
        progressBar = findViewById(R.id.progressBar1);
        textCarga = findViewById(R.id.textCarga);
    }

    public void convertirMoneda(View view) {
        String valor = editTextCantidad.getText().toString();

        if (!valor.isEmpty()) {
            double cantidad = Double.parseDouble(valor);

            // Inicializa progreso
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            textCarga.setVisibility(View.GONE);
            textResultado.setText("");

            Handler handler = new Handler();
            new Thread(() -> {
                for (int i = 0; i <= 100; i += 10) {
                    int finalI = i;
                    handler.postDelayed(() -> progressBar.setProgress(finalI), finalI * 10);
                }

                handler.postDelayed(() -> {
                    double resultado = cantidad * 3.7;
                    String resultadoFormateado = String.format("%.2f", resultado);
                    textResultado.setText("S/. " + resultadoFormateado);

                    textCarga.setText("Carga completa");
                    textCarga.setVisibility(View.VISIBLE);
                }, 1200);

            }).start();

        } else {
            textResultado.setText("Ingrese una cantidad válida");
        }
    }

    public void volverAMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

