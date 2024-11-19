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

public class TablesActivity extends AppCompatActivity {

    private TableAdapter tableAdapter;
    private ListView listView;

    public static ArrayList<Table> tables = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_menu);

        if (tables == null) {
            tables = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Table table = new Table(i);
                tables.add(table);
            }
        }
        listView = findViewById(R.id.listView);
        tableAdapter = new TableAdapter(this, tables);
        listView.setAdapter(tableAdapter);
    }


}


