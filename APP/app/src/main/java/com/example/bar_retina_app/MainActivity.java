package com.example.bar_retina_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    private EditText serverUrlEditText;
    private EditText userNameEditText;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.activity_main);

        serverUrlEditText = findViewById(R.id.serverUrl);
        userNameEditText = findViewById(R.id.userName);
        connectButton = findViewById(R.id.connectButton);


        connectButton.setOnClickListener(view -> {
            String serverUrl = serverUrlEditText.getText().toString().trim();
            if (!serverUrl.isEmpty()) {
                ConnectToServer(serverUrl);
            } else {
                Toast.makeText(this, "Please enter a valid WebSocket URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void ConnectToServer(String url) {
        WebSocketClient client = null;

        try {
            client = new WebSocketClient(new URI(url), (Draft) new Draft_6455()) {
                @Override
                public void onMessage(String message) { onMessageListener(message); }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to: " + getURI());

                    try {
                        JSONObject message = new JSONObject();

                        message.put("message", "Hola");
                        message.put("type", "connection");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from: " + getURI());
                }

                @Override
                public void onError(Exception ex) { ex.printStackTrace(); }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + url + " no és una direcció URI de WebSocket vàlida");
        }
    }

    protected void onMessageListener (String message) {
        System.out.println(message + "\n");
    }
}