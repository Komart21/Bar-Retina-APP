package com.example.bar_retina_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> productos;

    // Constructor
    public CustomAdapter(Context context, List<Product> productos) {
        super(context, 0, productos);  // Llamada al constructor base de ArrayAdapter
        this.context = context;
        this.productos = productos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_product, parent, false);
        }

        Product producto = productos.get(position);
        TextView nameTextView = convertView.findViewById(R.id.productName);
        nameTextView.setText(producto.getName());

        TextView descriptionTextView = convertView.findViewById(R.id.productDescription);
        descriptionTextView.setText(producto.getDescription());

        TextView priceTextView = convertView.findViewById(R.id.productPrice);
        priceTextView.setText("$" + producto.getPrice());

        EditText quantityEditText = convertView.findViewById(R.id.productQuantity);
        quantityEditText.setText("1");

        Button addToOrderButton = convertView.findViewById(R.id.addToOrderButton);
        addToOrderButton.setOnClickListener(v -> {
            String cantidadStr = quantityEditText.getText().toString();
            int cantidad = 1; // Valor por defecto

            try {
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Cantidad no válida", Toast.LENGTH_SHORT).show();
                return;
            }

            String mensaje = "Producto agregado: " + producto.getName() + " - Cantidad: " + cantidad;
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }
}