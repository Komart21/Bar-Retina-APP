package com.example.bar_retina_app;

import static java.lang.System.out;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ConfigActivity extends AppCompatActivity {
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_config);

        EditText url = findViewById(R.id.url);
        EditText name = findViewById(R.id.name);

        file = new File(getFilesDir(), "CONFIG.xml");

        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        Button buttonBack = findViewById(R.id.buttonBack);

        buttonUpdate.setOnClickListener(view -> {
            String newURL = url.getText().toString();
            String newName = name.getText().toString();

            if (newURL.isEmpty() || newName.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            deleteXML();

            createXML(newName, newURL);
            Toast.makeText(this, "XML file updated successfully", Toast.LENGTH_SHORT).show();
        });

        buttonBack.setOnClickListener(view -> {
            Intent intent = new Intent(ConfigActivity.this, TablesActivity.class);
            startActivity(intent);
        });
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
