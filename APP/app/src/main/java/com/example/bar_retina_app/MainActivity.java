package com.example.bar_retina_app;

import static java.lang.System.out;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class MainActivity extends AppCompatActivity {
    private EditText serverUrlEditText;
    private EditText userNameEditText;
    private Button connectButton;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        file = new File(getFilesDir(), "CONFIG.xml");

        if (file.exists()) {

            String url = getURL();
            Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

            ConnectToServer(url);

            Toast.makeText(this, "Connected to: " + url, Toast.LENGTH_SHORT).show();

            setContentView(R.layout.main_menu);
        } else {
            setContentView(R.layout.login_menu);

            serverUrlEditText = findViewById(R.id.serverUrl);
            userNameEditText = findViewById(R.id.userName);
            connectButton = findViewById(R.id.connectButton);


            connectButton.setOnClickListener(view -> {

                String serverUrl = serverUrlEditText.getText().toString().trim();
                String userName = userNameEditText.getText().toString().trim();
                if (!serverUrl.isEmpty()) {
                    ConnectToServer(serverUrl);
                    Toast.makeText(this, "Connected to: " + serverUrl, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please enter a valid WebSocket URL", Toast.LENGTH_SHORT).show();
                }

                createXML(userName, serverUrl);
                setContentView(R.layout.main_menu);
            });
        }
        }

    protected void ConnectToServer(String url) {
        WebSocketClient client = null;

        try {
            client = new WebSocketClient(new URI(url), (Draft) new Draft_6455()) {
                @Override
                public void onMessage(String message) { onMessageListener(message); }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    out.println("Connected to: " + getURI());

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
                    out.println("Disconnected from: " + getURI());
                }

                @Override
                public void onError(Exception ex) { ex.printStackTrace(); }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            out.println("Error: " + url + " no és una direcció URI de WebSocket vàlida");
        }
    }

    protected void onMessageListener (String message) {
        out.println(message + "\n");
    }

    private void createXML(String userName, String URL) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element elmRoot = doc.createElement("login");
            doc.appendChild(elmRoot);
            Element elmLocation = doc.createElement("username");
            Text nodeLocation = doc.createTextNode(userName);
            elmLocation.appendChild(nodeLocation);
            elmRoot.appendChild(elmLocation);
            Element elmUrl = doc.createElement("URL");
            Text nodeUrl = doc.createTextNode(URL);
            elmUrl.appendChild(nodeUrl);
            elmRoot.appendChild(elmUrl);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            File outputFile = new File(String.valueOf(file));
            StreamResult result = new StreamResult(outputFile);

            transformer.transform(source, result);

            out.println("XML file saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getURL() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            // Expresión XPath para seleccionar el elemento URL
            XPathExpression expr = xPath.compile("/login/URL");

            // Evaluar la expresión XPath y obtener el valor del nodo
            return (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}