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

public class MainActivity extends AppCompatActivity {
    private WebSocket webSocket;
    private OkHttpClient client;
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

        client = new OkHttpClient();

        connectButton.setOnClickListener(view -> {
            String serverUrl = serverUrlEditText.getText().toString().trim();
            if (!serverUrl.isEmpty()) {
                connectToWebSocket(serverUrl);
            } else {
                Toast.makeText(this, "Please enter a valid WebSocket URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectToWebSocket(String url) {
        Request request = new Request.Builder().url(url).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show());
                String userName = userNameEditText.getText().toString().trim();
                webSocket.send("Hello, " + userName);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Received: " + text, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Received bytes", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Closing: " + reason, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show());
                runOnUiThread(() -> System.out.println("Error: " + t.getMessage()));
            }
        });

        client.dispatcher().executorService().shutdown();
    }
}