package com.example.bar_retina_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationsActivity extends AppCompatActivity {
    public static ArrayList<Notification> notifications = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_menu);

        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        String hour = sdf2.format(new Date());
        String readyMessage = "String de prueba";
        notifications.add(new Notification(readyMessage, hour));



        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsActivity.this, TablesActivity.class);
                startActivity(intent);
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifications.clear();
            }
        });

        NotificationAdapter adapter = new NotificationAdapter(this, notifications);
        ListView listView = findViewById(R.id.notificationsListView);
        listView.setAdapter(adapter);

    }
}
