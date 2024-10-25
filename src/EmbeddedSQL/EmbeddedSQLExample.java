package EmbeddedSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmbeddedSQLExample {

    public static void main(String[] args) {
        // Predefined (embedded) values for member details
        int member_id = 5;
        String member_name = "Robert Knight";
        String member_email = "robert.knight@example.com";
        String member_phone = "0734568908";

        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/library_system"; // Replace with your database name
        String dbUser = "root"; // Replace with your MySQL username
        String dbPassword = "Cyril@2019"; // Replace with your MySQL password

        Connection connection = null;

        try {
            // Establish connection to the database
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Predefined SQL statement to insert a member (Embedded SQL)
            String insertQuery = "INSERT INTO members (MemberID, Name, Email, Phone) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setInt(1, member_id);
            insertStmt.setString(2, member_name);
            insertStmt.setString(3, member_email);
            insertStmt.setString(4, member_phone);

            // Execute the predefined query (insert operation)
            insertStmt.executeUpdate();
            System.out.println("Member inserted successfully!");

            // Predefined SQL statement to retrieve member details (Embedded SQL)
            String selectQuery = "SELECT Name, Email, Phone FROM members WHERE MemberID = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setInt(1, member_id);

            // Execute the predefined query (select operation)
            ResultSet resultSet = selectStmt.executeQuery();

            // Process the result set
            if (resultSet.next()) {
                String retrievedName = resultSet.getString("Name");
                String retrievedEmail = resultSet.getString("Email");
                String retrievedPhone = resultSet.getString("Phone");

                System.out.println("Member Name: " + retrievedName);
                System.out.println("Member Email: " + retrievedEmail);
                System.out.println("Member Phone: " + retrievedPhone);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the connection
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

