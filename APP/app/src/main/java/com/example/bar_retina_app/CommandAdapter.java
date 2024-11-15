package com.example.bar_retina_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CommandAdapter extends ArrayAdapter<CommandProduct> {

    public CommandAdapter(Context context, List<CommandProduct> products) {
        super(context, 0, products);
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
        itemQuantity.setText("x" + currentItem.getQuantity());

        return convertView;
    }
}