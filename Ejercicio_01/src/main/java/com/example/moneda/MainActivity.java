package com.example.moneda;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText text;
    private Spinner spinnerFrom, spinnerTo;
    private Button btnConvertir;
    private final Map<String, Double> exchangeRates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos del layout
        text = findViewById(R.id.editTextText);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        btnConvertir = findViewById(R.id.btnConvertir);

        // Configurar Spinner con las monedas
        String[] monedas = {"USD", "PEN", "EUR", "GBP", "INR", "BRL", "MXN", "CNY", "JPY"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monedas);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Configurar tasas de conversión (Ejemplo: 1 USD = X moneda)
        configurarTasasDeCambio();

        // Manejo de clic en el botón de conversión
        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertirMoneda();
            }
        });
    }

    // Configurar tasas de conversión
    private void configurarTasasDeCambio() {
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("PEN", 3.8);
        exchangeRates.put("EUR", 0.92);
        exchangeRates.put("GBP", 0.78);
        exchangeRates.put("INR", 83.0);
        exchangeRates.put("BRL", 5.2);
        exchangeRates.put("MXN", 17.0);
        exchangeRates.put("CNY", 7.2);
        exchangeRates.put("JPY", 150.0);
    }

    // Método para realizar la conversión
    private void convertirMoneda() {
        if (text.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingresa un número válido", Toast.LENGTH_LONG).show();
            return;
        }

        double cantidad = Double.parseDouble(text.getText().toString());
        String monedaOrigen = spinnerFrom.getSelectedItem().toString();
        String monedaDestino = spinnerTo.getSelectedItem().toString();

        if (monedaOrigen.equals(monedaDestino)) {
            Toast.makeText(this, "Selecciona monedas diferentes", Toast.LENGTH_SHORT).show();
            return;
        }

        double tasaOrigen = exchangeRates.get(monedaOrigen);
        double tasaDestino = exchangeRates.get(monedaDestino);

        double resultado = (cantidad / tasaOrigen) * tasaDestino;
        text.setText(String.format("%.2f", resultado));
    }
}


