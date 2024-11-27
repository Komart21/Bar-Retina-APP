package com.example.bar_retina_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    public NotificationAdapter(Context context, List<Notification> notifications) {
        super(context, 0, notifications);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification, parent, false);
        }

        Notification currentItem = getItem(position);

        TextView notificationTitle = convertView.findViewById(R.id.notificationTitle);
        assert currentItem != null;
        notificationTitle.setText(currentItem.getMessage());
        TextView notificationDate = convertView.findViewById(R.id.notificationDate);
        notificationDate.setText(currentItem.getDate());

        ImageButton actionButton = convertView.findViewById(R.id.actionButton);

        actionButton.setOnClickListener(v -> {
            remove(currentItem);
            notifyDataSetChanged();
        });


        return convertView;
    }
}