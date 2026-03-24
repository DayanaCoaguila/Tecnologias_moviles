package com.example.pizza;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Nombres de las pizzas
    String[] pizzaNames = {"Pepperoni", "Americana", "Vegetariana", "Hawaiana"};

    // Descripción de cada pizza
    String[] pizzaDescriptions = {
            "Deliciosa pizza con rodajas de pepperoni.",
            "Clásica pizza con queso y salsa de tomate.",
            "Pizza con variedad de vegetales frescos.",
            "Pizza con piña y jamón, combinación agridulce."
    };

    // Imágenes de las pizzas (Asegúrate de tener hawaiana.jpg en res/drawable)
    int[] pizzaImages = {
            R.drawable.pepperoni,
            R.drawable.americana,
            R.drawable.vegetariana,
            R.drawable.hawaiana
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter());

        gridView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(MainActivity.this, pizzaDescriptions[position], Toast.LENGTH_SHORT).show());
    }

    // Adaptador interno para manejar la grilla de imágenes y nombres
    private class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pizzaNames.length;
        }

        @Override
        public Object getItem(int position) {
            return pizzaNames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.grid_item, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView textView = convertView.findViewById(R.id.textView);

            imageView.setImageResource(pizzaImages[position]);
            textView.setText(pizzaNames[position]);

            return convertView;
        }
    }
}


