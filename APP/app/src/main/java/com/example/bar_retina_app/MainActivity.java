package com.example.bar_retina_app;

import static java.lang.System.out;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

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

    public static ArrayList<Element> productes = new ArrayList<>();
    public static ArrayList<Product> objProductes = new ArrayList<>();
    public static String waiterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file = new File(getFilesDir(), "CONFIG.xml");

        if (file.exists()) {

            String url = getURL();
            waiterName = getName();

            ConnectToServer(url);

            Toast.makeText(this, "Connected to: " + url, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, TablesActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.login_menu);

            serverUrlEditText = findViewById(R.id.serverUrl);
            userNameEditText = findViewById(R.id.userName);
            connectButton = findViewById(R.id.connectButton);


            connectButton.setOnClickListener(view -> {

                String serverUrl = serverUrlEditText.getText().toString().trim();
                String userName = userNameEditText.getText().toString().trim();
                waiterName = userName;

                if (!serverUrl.isEmpty()) {
                    ConnectToServer(serverUrl);
                    Log.i("WSS Client", "Connected to: " + serverUrl);
                } else {
                    Toast.makeText(this, "Please enter a valid WebSocket URL", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(MainActivity.this, TablesActivity.class);
                startActivity(intent);
                createXML(userName, serverUrl);
            });
        }
        }

    protected void ConnectToServer(String url) {
        WebSocketClient client;

        try {
            client = new WebSocketClient(new URI(url), new Draft_6455()) {
                @Override
                public void onMessage(String message) {
                    try {
                        JSONObject msgObj = new JSONObject(message);
                        switch (msgObj.getString("type")) {
                            case "ping":
                                String pong = msgObj.getString("message");
                                out.println(pong);
                                break;
                            case "bounce":
                                String msg = msgObj.getString("message");
                                out.println(msg);
                                break;
                            case "products":
                                String xmlString = msgObj.getString("message");
                                Log.d("WebSocket", "XML recibido: " + xmlString);

                                try {
                                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                    DocumentBuilder builder = factory.newDocumentBuilder();
                                    InputSource is = new InputSource(new StringReader(xmlString));
                                    Document document = builder.parse(is);

                                    NodeList products = document.getElementsByTagName("product");
                                    for (int i = 0; i < products.getLength(); i++) {
                                        Element product = (Element) products.item(i);
                                        productes.add(product);
                                    }

                                    for (Element producte : productes) {
                                        String id = producte.getAttribute("id");
                                        String tags = producte.getAttribute("tags");
                                        NodeList nodeList0 = producte.getElementsByTagName("name");
                                        String name = nodeList0.item(0).getTextContent();
                                        NodeList nodeList1 = producte.getElementsByTagName("description");
                                        String description = nodeList1.item(0).getTextContent();
                                        NodeList nodeList2 = producte.getElementsByTagName("price");
                                        String price = (nodeList2.item(0).getTextContent());
                                        NodeList nodeList3 = producte.getElementsByTagName("image");
                                        String image = nodeList3.item(0).getTextContent();

                                        Product product = new Product(id, tags, name, description, price, image);
                                        ProductsActivity.products.add(product);
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace(); // Manejo de las excepciones
                                }
                                break;
                            case "productswithimage":
                                JSONArray array = msgObj.getJSONArray("message");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject product = array.getJSONObject(i);

                                    String id = product.getString("id");
                                    String tags = product.getString("tags");
                                    String name = product.getString("name");
                                    String description = product.getString("description");
                                    String price = product.getString("price");
                                    String image = product.getString("image");

                                    Product Obj = new Product(id, tags, name, description, price, image);
                                    ProductsActivity.products.add(Obj);

                                }
                                Toast.makeText(getApplicationContext(), "Json correctly loaded", Toast.LENGTH_SHORT).show();
                                break;


                            case "tags":
                                String tags = msgObj.getString("message");
                                System.out.println(tags);
                                break;
                            case "ready":
                                String readyMessage = msgObj.getString("message");
                                Log.d("WebSocket", "Message recieved: " + readyMessage);
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), readyMessage, Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void onOpen(ServerHandshake handshake) {
                    out.println("Connected to: " + getURI());

                    try {
                        JSONObject message = new JSONObject();
                        message.put("message", "products");
                        message.put("type", "productswithimage");
                        send(message.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    out.println("Disconnected from: " + getURI());
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            out.println("Error: " + url + " no es una dirección URI de WebSocket válida");
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

            // Evaluar la            // Evaluar la expresión XPath y obtener el valor del nodo expresión XPath y obtener el valor del nodo
            return (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getName() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            // Expresión XPath para seleccionar el elemento URL
            XPathExpression expr = xPath.compile("/login/username");

            // Evaluar la expresión XPath y obtener el valor del nodo
            return (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteXML() {
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Toast.makeText(this, "XML file deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete XML file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "XML file does not exist", Toast.LENGTH_SHORT).show();
        }
    }
}