package com.example.proyecto3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SegundaActividad extends AppCompatActivity {
    protected static final int TIMER_RUNTIME = 10000; // 10 segundos
    protected boolean nbActivo;
    protected ProgressBar nProgressBar;
    protected TextView textCarga; // NUEVO

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_actividad);

        nProgressBar = findViewById(R.id.progressBar1);
        textCarga = findViewById(R.id.textCarga); // NUEVO

        final Thread timerThread = new Thread() {
            @Override
            public void run() {
                nbActivo = true;
                try {
                    int espera1 = 0;
                    while (nbActivo && (espera1 < TIMER_RUNTIME)) {
                        sleep(200);
                        if (nbActivo) {
                            espera1 += 200;
                            actualizarProgress(espera1);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(() -> onContinuar()); // IMPORTANTE para manipular UI
                }
            }
        };
        timerThread.start();
    }

    public void actualizarProgress(final int timePassed) {
        if (nProgressBar != null) {
            final int progress = nProgressBar.getMax() * timePassed / TIMER_RUNTIME;
            runOnUiThread(() -> nProgressBar.setProgress(progress));
        }
    }

    public void onContinuar() {
        Log.d("mensajeFinal", "Su barra de progreso acaba de cargar");
        if (textCarga != null) {
            textCarga.setVisibility(View.VISIBLE);
        }
    }

    public void volverAMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


