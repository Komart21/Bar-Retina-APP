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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateCommandActivity extends AppCompatActivity {
    public static int currentTableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_order_menu);

        TextView tableId = findViewById(R.id.tableId);
        tableId.setText("Table " + currentTableId);

        Button comandaButton = findViewById(R.id.createOrderButton);

        comandaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablesActivity.tables.get(currentTableId).setCommand(new Command(1, currentTableId, "Pepe"));
                Intent intent = new Intent(CreateCommandActivity.this, CommandActivity.class);
                startActivity(intent);

            }
        });

    }
}


