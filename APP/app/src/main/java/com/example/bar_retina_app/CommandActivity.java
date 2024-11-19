package com.example.bar_retina_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class CommandActivity extends AppCompatActivity {
    public static int currentTableId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_menu);

        TextView tableId = findViewById(R.id.tableId);
        tableId.setText("Table " + currentTableId);

        Button addButton = findViewById(R.id.addButton); // Encontramos el botón
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsActivity.currentTableId = currentTableId;
                Intent intent = new Intent(CommandActivity.this, ProductsActivity.class);
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.backButton); // Encontramos el botón
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommandActivity.this, TablesActivity.class);
                startActivity(intent);
            }
        });

        List<CommandProduct> groupedProducts = TablesActivity.tables.get(currentTableId).getCommand().getGroupedProducts();

        CommandAdapter adapter = new CommandAdapter(this, groupedProducts);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
