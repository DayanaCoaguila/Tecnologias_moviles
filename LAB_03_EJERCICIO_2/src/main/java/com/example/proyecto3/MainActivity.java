package com.example.proyecto3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String eti = "CicloDeVida";
    ProgressBar progressBar; // NUEVO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(eti, "En el evento onCreate()");

        // Inicializa la ProgressBar
        progressBar = findViewById(R.id.progressBar1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(eti, "En el evento onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(eti, "En el evento onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(eti, "En el evento onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(eti, "En el evento onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(eti, "En el evento onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(eti, "En el evento onDestroy()");
    }

    public void onClick(View view) {
        // Muestra la barra y simula carga
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Carga completa", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SegundaActividad.class);
            startActivity(intent);
        }, 2000); // Simula 2 segundos de carga
    }

    public void abrirMenuActividades(View view) {
        // Igual aquí puedes usar la barra si deseas
        Intent intent = new Intent(this, DrawerActivity.class);
        startActivity(intent);
    }
}



