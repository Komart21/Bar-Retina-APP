package com.example.bar_retina_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CommandAdapter extends ArrayAdapter<CommandProduct> {

    private int currentTableId;

    public CommandAdapter(Context context, List<CommandProduct> products, int currentTableId) {
        super(context, 0, products);
        this.currentTableId = currentTableId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_command_details, parent, false);
        }

        CommandProduct currentItem = getItem(position);

        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemQuantity = convertView.findViewById(R.id.itemQuantity);

        itemName.setText(currentItem.getProduct().getName());
        itemQuantity.setText(String.valueOf(currentItem.getQuantity()));

        Button addToOrderButton = convertView.findViewById(R.id.increaseButton);
        addToOrderButton.setOnClickListener(v -> {
            TablesActivity.tables.get(currentTableId).getCommand().addProduct(currentItem.getProduct());
            itemQuantity.setText(String.valueOf(currentItem.getQuantity()));
            notifyDataSetChanged();
        });
        Button removeFromOrderButton = convertView.findViewById(R.id.decreaseButton);
        removeFromOrderButton.setOnClickListener(v -> {
            TablesActivity.tables.get(currentTableId).getCommand().removeProduct(currentItem.getProduct());
            itemQuantity.setText(String.valueOf(currentItem.getQuantity()));
            notifyDataSetChanged();
        });


        return convertView;
    }
}