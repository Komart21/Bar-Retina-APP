package com.example.bar_retina_app;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class CommandActivity extends AppCompatActivity {
    public static Command command = new Command(1, "Table 5", "John");

    public static String table = command.getTable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_menu);

        TextView tableId = findViewById(R.id.tableId);
        tableId.setText(table);

        List<CommandProduct> groupedProducts = command.getGroupedProducts();

        CommandAdapter adapter = new CommandAdapter(this, groupedProducts);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
