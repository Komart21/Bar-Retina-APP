package com.example.bar_retina_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TableAdapter extends ArrayAdapter<Table> {

    public TableAdapter(Context context, List<Table> tables) {
        super(context, 0, tables);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_command_details, parent, false);
        }

        Table currentItem = getItem(position);

        TextView tableName = convertView.findViewById(R.id.tableName);
        TextView orderDescription = convertView.findViewById(R.id.orderDescription);

        tableName.setText("Table " + currentItem.getTableId());
        if (currentItem.getCommand() == null) {
            orderDescription.setText("This table has no orders.");
        }
        else {
            orderDescription.setText("This table has an order pending.");
        }

        Button viewOrder = convertView.findViewById(R.id.actionButton);
        viewOrder.setOnClickListener(v -> {
            if (currentItem.getCommand() == null) {
                Context a = getContext();
                Intent intent = new Intent(a, CreateCommandActivity.class);
                a.startActivity(intent);
            }
        });

        return convertView;
    }
}
