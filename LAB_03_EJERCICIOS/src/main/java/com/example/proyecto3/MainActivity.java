package com.example.proyecto3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String eti = "CicloDeVida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(eti, "En el evento onCreate()");
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
        Intent intent = new Intent(this, SegundaActividad.class);
        startActivity(intent);
    }

    // 👉 Nuevo método para ir a DrawerActivity
    public void abrirMenuActividades(View view) {
        Intent intent = new Intent(this, DrawerActivity.class);
        startActivity(intent);
    }
}


