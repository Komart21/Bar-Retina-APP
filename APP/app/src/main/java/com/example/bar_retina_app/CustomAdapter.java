package com.example.bar_retina_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import android.util.Base64;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> productos;
    private int currentTableId;

    // Constructor
    public CustomAdapter(Context context, List<Product> productos, int currentTableId) {
        super(context, 0, productos);  // Llamada al constructor base de ArrayAdapter
        this.context = context;
        this.productos = productos;
        this.currentTableId = currentTableId;
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

        ImageView imageView = convertView.findViewById(R.id.productImage);
        loadImageToView(imageView, producto.getImage());

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
            for (int i = 1; i <= cantidad; i++) {
                TablesActivity.tables.get(currentTableId).getCommand().addProduct(producto);
            }

            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    private void loadImageToView(ImageView imageView, String base64Image) {
        try {
            // Eliminar espacios en blanco del string Base64
            base64Image = base64Image.replaceAll(" ", "");
            byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);

            // Convertir el arreglo de bytes en un Bitmap
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(decodedBitmap);

        } catch (IllegalArgumentException e) {
            Log.e("ImageLoading", "Error decoding Base64 image: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e("ImageLoading", "Error loading image: " + e.getMessage(), e);
        }
    }
}