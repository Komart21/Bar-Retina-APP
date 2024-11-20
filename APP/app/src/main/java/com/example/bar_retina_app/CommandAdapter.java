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
    private List<CommandProduct> products;
    private CommandActivity activity;

    public CommandAdapter(Context context, CommandActivity activity, List<CommandProduct> products, int currentTableId) {
        super(context, 0, products);
        this.currentTableId = currentTableId;
        this.products = products;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_command_details, parent, false);
        }

        CommandProduct currentItem = getItem(position);

        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemQuantity = convertView.findViewById(R.id.itemQuantity);
        TextView itemPrice = convertView.findViewById(R.id.itemPrice);

        itemName.setText(currentItem.getProduct().getName());
        itemQuantity.setText(String.valueOf(currentItem.getQuantity()));
        itemPrice.setText(currentItem.getProduct().getPrice() + "€");

        Button addToOrderButton = convertView.findViewById(R.id.increaseButton);
        addToOrderButton.setOnClickListener(v -> {
            TablesActivity.tables.get(currentTableId).getCommand().addProduct(currentItem.getProduct());
            currentItem.incrementQuantity();
            itemQuantity.setText(String.valueOf(currentItem.getQuantity()));
            updateTotalPrice();
            notifyDataSetChanged();
        });
        Button removeFromOrderButton = convertView.findViewById(R.id.decreaseButton);
        removeFromOrderButton.setOnClickListener(v -> {
            TablesActivity.tables.get(currentTableId).getCommand().removeProduct(currentItem.getProduct());
            currentItem.decrementQuantity();
            itemQuantity.setText(String.valueOf(currentItem.getQuantity()));
            if (currentItem.getQuantity() <= 0) {
                products.remove(currentItem);
            }

            updateTotalPrice();

            notifyDataSetChanged();
        });


        return convertView;
    }

    private void updateTotalPrice() {
        float total = TablesActivity.tables.get(currentTableId).getCommand().getTotalPrice();
        activity.totalPrice.setText(String.format("%.2f", total) + "€");
    }
}