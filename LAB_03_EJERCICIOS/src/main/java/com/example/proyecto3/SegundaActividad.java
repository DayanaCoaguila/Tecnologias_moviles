package com.example.proyecto3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class SegundaActividad extends AppCompatActivity {
    protected static final int TIMER_RUNTIME = 10000; // 10 segundos
    protected boolean nbActivo;
    protected ProgressBar nProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_actividad);

        nProgressBar = findViewById(R.id.progressBar);

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
                    onContinuar();
                }
            }
        };
        timerThread.start();
    }

    public void actualizarProgress(final int timePassed) {
        if (nProgressBar != null) {
            final int progress = nProgressBar.getMax() * timePassed / TIMER_RUNTIME;
            nProgressBar.setProgress(progress);
        }
    }

    public void onContinuar() {
        Log.d("mensajeFinal", "Su barra de progreso acaba de cargar");
    }

    // Método para volver a MainActivity
    public void volverAMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra esta actividad
    }
}

