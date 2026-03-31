package com.example.proyecto3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DrawerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Button btnMainActivity = findViewById(R.id.btnMainActivity);
        Button btnConversion = findViewById(R.id.btnConversion);
        Button btnSegunda = findViewById(R.id.btnSegunda);

        btnMainActivity.setOnClickListener(v ->
                startActivity(new Intent(DrawerActivity.this, MainActivity.class))
        );
        btnConversion.setOnClickListener(v ->
                startActivity(new Intent(DrawerActivity.this, ConversionMonedaActivity.class))
        );
        btnSegunda.setOnClickListener(v ->
                startActivity(new Intent(DrawerActivity.this, SegundaActividad.class))
        );
    }
}

