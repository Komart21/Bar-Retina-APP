package com.example.bar_retina_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter productoAdapter;

    private Spinner tagSpinner;
    public static ArrayList<Product> products = new ArrayList<>();

    private ArrayList<Product> productosFiltrados = new ArrayList<>();

    private List<String> predefinedTags = new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3", "tag4", "Todos"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        products.add(new Product("1", "tag1", "Product 1", "Descripción 1", "10.0", "image_url_1"));
        products.add(new Product("2", "Product 2", "Product 2", "Descripción 2", "15.0", "image_url_2"));
        products.add(new Product("3", "Product 3", "Product 3", "Descripción 3", "20.0", "image_url_3"));
        products.add(new Product("4", "Product 4", "Product 4", "Descripción 4", "25.0", "image_url_4"));

        // Copiamos todos los productos a la lista filtrada inicialmente
        productosFiltrados.addAll(products);

        // Configurar ListView
        listView = findViewById(R.id.listView);
        productoAdapter = new CustomAdapter(this, productosFiltrados);
        listView.setAdapter(productoAdapter);

        // Configurar Spinner con las tags
        tagSpinner = findViewById(R.id.tagSpinner);

        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, predefinedTags);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTag = parentView.getItemAtPosition(position).toString();
                if (selectedTag.equals("Todos")) {
                    showAllProducts();
                } else {
                    filterProductsByTag(selectedTag);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                showAllProducts();
            }
        });

        // Mostrar todos los productos al iniciar la actividad
        showAllProducts();
    }

    // Mostrar todos los productos
    private void showAllProducts() {
        productosFiltrados.clear();
        productosFiltrados.addAll(products);
        productoAdapter.notifyDataSetChanged(); // Actualizar la vista
    }

    // Filtrar productos por tag
    private void filterProductsByTag(String tag) {
        productosFiltrados.clear();
        productosFiltrados.addAll(products.stream()
                .filter(producto -> producto.getTags().toLowerCase().contains(tag.toLowerCase()))
                .collect(Collectors.toList()));

        if (productosFiltrados.isEmpty()) {
            Toast.makeText(ProductsActivity.this, "No se encontraron productos con esta tag.", Toast.LENGTH_SHORT).show();
        }

        productoAdapter.notifyDataSetChanged(); // Actualizar la vista
    }
}


