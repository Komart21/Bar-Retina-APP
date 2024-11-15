package com.example.bar_retina_app;

import static java.util.Collections.sort;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    public static Command command;

    private Spinner tagSpinner;
    public static ArrayList<Product> products = new ArrayList<>();

    private ArrayList<Product> productosFiltrados = new ArrayList<>();

    private static ArrayList<String> temptags = new ArrayList<>(Arrays.asList("soda", "zero-sugar", "caffeine-free", "water", "non-carbonated",
            "sparkling water", "isotonic", "beer", "alcohol-free", "baguette",
            "eggs", "meat", "lactose", "seafood", "spicy", "poultry",
            "vegetarian", "sandwich", "mixed", "burger", "main", "fish",
            "starter", "potato"));

    private static ArrayList<String> tags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button comandaButton = findViewById(R.id.comandaButton);

        comandaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, CommandActivity.class);
                startActivity(intent);
            }
        });

        sort(temptags);
        tags.add("all");
        for (String tag : temptags) {
            tags.add(tag);
        }

        productosFiltrados.addAll(products);

        listView = findViewById(R.id.listView);
        productoAdapter = new CustomAdapter(this, productosFiltrados);
        listView.setAdapter(productoAdapter);

        tagSpinner = findViewById(R.id.tagSpinner);

        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tags);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTag = parentView.getItemAtPosition(position).toString();
                if (selectedTag.equals("all")) {
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

        showAllProducts();
    }

    private void showAllProducts() {
        productosFiltrados.clear();
        productosFiltrados.addAll(products);
        productoAdapter.notifyDataSetChanged();
    }

    private void filterProductsByTag(String tag) {
        productosFiltrados.clear();
        productosFiltrados.addAll(products.stream()
                .filter(producto -> producto.getTags().toLowerCase().contains(tag.toLowerCase()))
                .collect(Collectors.toList()));

        if (productosFiltrados.isEmpty()) {
            Toast.makeText(ProductsActivity.this, "No se encontraron productos con esta tag.", Toast.LENGTH_SHORT).show();
        }

        productoAdapter.notifyDataSetChanged();
    }


}


