package com.example.bar_retina_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.model.LocalDate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


public class CommandActivity extends AppCompatActivity {
    public TextView totalPrice;
    public static int currentTableId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_menu);

        totalPrice = findViewById(R.id.totalPrice);

        TextView tableId = findViewById(R.id.tableId);
        int visualTableId = currentTableId + 1;
        tableId.setText("Table " + visualTableId);

        float total = TablesActivity.tables.get(currentTableId).getCommand().getTotalPrice();
        totalPrice.setText(String.format("%.2f", total) + "€");

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsActivity.currentTableId = currentTableId;
                Intent intent = new Intent(CommandActivity.this, ProductsActivity.class);
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommandActivity.this, TablesActivity.class);
                startActivity(intent);
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(new Date());

                // Obtener la ID de la comanda a partir del tableId y day
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/barretina2", "username", "password")) {
                    Integer commandId = getExistingCommandId(currentTableId, day, connection);

                    if (commandId != null) {
                        // Eliminar la comanda de la base de datos
                        deleteCommandFromDatabase(commandId, connection);

                        // Limpiar la comanda en la interfaz de usuario
                        TablesActivity.tables.get(currentTableId).setCommand(null);

                        // Volver a la pantalla de selección de mesa
                        Intent intent = new Intent(CommandActivity.this, TablesActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CommandActivity.this, "Command not found.", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(CommandActivity.this, "Failed to delete command.", Toast.LENGTH_SHORT).show();
                }

                TablesActivity.tables.get(currentTableId).setCommand(null);
                Intent intent = new Intent(CommandActivity.this, CreateCommandActivity.class);
                startActivity(intent);
            }
        });

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            // Datos principales de la comanda
            int tableIdD = currentTableId;
            String waiter = "Waiter Name"; // Cambia esto según tus datos
            String state = "Pending"; // Estado inicial de la comanda
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String day = sdf.format(new Date());
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            String hour = sdf2.format(new Date());

            // Productos de la comanda
            List<CommandProduct> products = TablesActivity.tables.get(currentTableId).getCommand().getGroupedProducts();

            // Conectar con la base de datos e insertar/actualizar datos
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/barretina2", "username", "password")) {
                connection.setAutoCommit(false); // Iniciar transacción

                // Inserta o actualiza la comanda
                int commandId = insertOrUpdateCommand(tableIdD, waiter, state, hour, day, connection);

                // Actualiza los detalles de la comanda
                updateCommandDetails(commandId, products, connection);

                connection.commit(); // Confirmar transacción
                Toast.makeText(this, "Command sent successfully!", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to send command.", Toast.LENGTH_SHORT).show();
            }
        });

        List<CommandProduct> groupedProducts = TablesActivity.tables.get(currentTableId).getCommand().getGroupedProducts();

        CommandAdapter adapter = new CommandAdapter(this, this, groupedProducts, currentTableId);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }



    public Integer getExistingCommandId(int tableId, String day, Connection connection) throws SQLException {
        String query = "SELECT id FROM Command WHERE tableid = ? AND day = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tableId);
            pstmt.setString(2, day);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Devuelve el ID si existe
                }
            }
        }
        return null; // Retorna null si no existe
    }

    public void deleteCommandFromDatabase(int commandId, Connection connection) throws SQLException {
        connection.setAutoCommit(false); // Usar transacciones para garantizar la consistencia

        try {
            // Eliminar los detalles de la comanda
            String deleteDetailsQuery = "DELETE FROM Command_details WHERE id_command = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteDetailsQuery)) {
                pstmt.setInt(1, commandId);
                pstmt.executeUpdate();
            }

            // Eliminar la comanda
            String deleteCommandQuery = "DELETE FROM Command WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteCommandQuery)) {
                pstmt.setInt(1, commandId);
                pstmt.executeUpdate();
            }

            // Confirmar transacción
            connection.commit();
            Toast.makeText(this, "Command deleted successfully!", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            connection.rollback(); // Revertir la transacción en caso de error
            e.printStackTrace();
            Toast.makeText(this, "Failed to delete command.", Toast.LENGTH_SHORT).show();
        }
    }

    public int insertOrUpdateCommand(int tableId, String waiter, String state, String hour, String day, Connection connection) throws SQLException {
        // Verifica si la comanda ya existe
        Integer existingCommandId = getExistingCommandId(tableId, day, connection);

        if (existingCommandId != null) {
            // Actualiza la comanda existente
            String updateQuery = "UPDATE Command SET waiter = ?, state = ?, hour = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
                pstmt.setString(1, waiter);
                pstmt.setString(2, state);
                pstmt.setString(3, hour);
                pstmt.setInt(4, existingCommandId);
                pstmt.executeUpdate();
            }
            return existingCommandId; // Retorna el ID de la comanda actualizada
        } else {
            // Inserta una nueva comanda
            return insertCommand(tableId, waiter, state, hour, day, connection);
        }
    }

    public int insertCommand(int tableId, String waiter, String state, String hour, String day, Connection connection) throws SQLException {
        String query = "INSERT INTO Command (tableid, waiter, state, hour, day) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, tableId);
            pstmt.setString(2, waiter);
            pstmt.setString(3, state);
            pstmt.setString(4, hour);
            pstmt.setString(5, day);
            pstmt.executeUpdate();

            // Retrieve generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated command ID
                } else {
                    throw new SQLException("Failed to retrieve generated ID for Command.");
                }
            }
        }
    }
    public void insertCommandDetails(int commandId, List<CommandProduct> products, Connection connection) throws SQLException {
        String query = "INSERT INTO Command_details (id_command, id_products, product_name, price, state) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (CommandProduct product : products) {
                pstmt.setInt(1, commandId);
                pstmt.setInt(2, Integer.parseInt(product.getProduct().getId()));
                pstmt.setString(3, product.getProduct().getName());
                pstmt.setBigDecimal(4, BigDecimal.valueOf(Float.parseFloat(product.getProduct().getPrice())));
                pstmt.setString(5, "Pending");
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }


    public void updateCommandDetails(int commandId, List<CommandProduct> products, Connection connection) throws SQLException {
        // Elimina los detalles existentes para esta comanda
        String deleteQuery = "DELETE FROM Command_details WHERE id_command = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, commandId);
            pstmt.executeUpdate();
        }

        // Inserta los nuevos detalles
        insertCommandDetails(commandId, products, connection);
    }

}
