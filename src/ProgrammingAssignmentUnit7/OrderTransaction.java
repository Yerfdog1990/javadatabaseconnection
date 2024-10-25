package ProgrammingAssignmentUnit7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderTransaction {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ordertransaction"; // Database name
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = "Cyril@2019"; // Replace with your database password

    public static void main(String[] args) {
        processOrder();
    }

    private static void processOrder() {
        // Create unique identifiers for product_id, user_id, and order_id
        String productId = "P001"; // Example product_id
        String userId = "U001";    // Example user_id
        String orderId = "O001";    // Example order_id

        int amount = 1;             // Amount is an integer
        int paymentAmount = 500;    // Payment amount
        Connection conn = null;     // Declare the connection variable outside

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            conn.setAutoCommit(false); // Start transaction

            // Check if stock is available before deducting
            String checkStockSQL = "SELECT stock FROM products WHERE product_id = ?";
            int stock;
            try (PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSQL)) {
                checkStockStmt.setString(1, productId);
                ResultSet resultSet = checkStockStmt.executeQuery();
                if (resultSet.next()) {
                    stock = resultSet.getInt("stock");
                } else {
                    throw new SQLException("Product not found");
                }
            }

            if (stock < amount) {
                conn.rollback(); // Insufficient stock, rollback the transaction
                System.out.println("Insufficient stock, transaction rolled back.");
                return; // Exit the method after rollback
            } else {
                // Deduct stock for the ordered product
                String updateStockSQL = "UPDATE products SET stock = stock - ? WHERE product_id = ?";
                try (PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL)) {
                    updateStockStmt.setInt(1, amount); // Use int amount
                    updateStockStmt.setString(2, productId);
                    updateStockStmt.executeUpdate();
                }

                // Insert order record into the transaction table
                String insertTransactionSQL = "INSERT INTO transaction (order_id, user_id, product_id, amount) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertTransactionStmt = conn.prepareStatement(insertTransactionSQL)) {
                    insertTransactionStmt.setString(1, orderId); // Ensure orderId is treated as a string
                    insertTransactionStmt.setString(2, userId);
                    insertTransactionStmt.setString(3, productId);
                    insertTransactionStmt.setInt(4, amount); // Use int amount
                    insertTransactionStmt.executeUpdate();
                }

                // Deduct payment from user account (if applicable)
                String updateAccountSQL = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
                try (PreparedStatement updateAccountStmt = conn.prepareStatement(updateAccountSQL)) {
                    updateAccountStmt.setInt(1, paymentAmount);
                    updateAccountStmt.setString(2, userId);
                    updateAccountStmt.executeUpdate();
                }

                conn.commit(); // Commit the transaction if successful
                System.out.println("Transaction committed successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error during transaction: " + e.getMessage());
            // Rollback if there was an error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
        } finally {
            // Ensure the connection is closed
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error closing connection: " + closeEx.getMessage());
                }
            }
        }
    }
}
