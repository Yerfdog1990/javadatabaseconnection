package EmbeddedSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MembersExample {
    public static void main(String[] args) {
        // Host variables in Java (these can be changed to match new data)
        int member_id = 4;
        String member_name = "Alice Brown";
        String member_email = "alice.brown@example.com";
        String member_phone = "0712345678";

        // EmbeddedSQL.Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/library_system"; // Replace with your database name
        String dbUser = "root"; // Replace with your MySQL username
        String dbPassword = "Cyril@2019"; // Replace with your MySQL password

        Connection connection = null;

        try {
            // Establish connection to the database
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Insert data into the members table using host variables
            String insertQuery = "INSERT INTO members (MemberID, Name, Email, Phone) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setInt(1, member_id);
            insertStmt.setString(2, member_name);
            insertStmt.setString(3, member_email);
            insertStmt.setString(4, member_phone);
            insertStmt.executeUpdate();

            System.out.println("Member inserted successfully!");

            // Select data from the members table using host variables
            String selectQuery = "SELECT Name, Email, Phone FROM members WHERE MemberID = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setInt(1, member_id);

            ResultSet resultSet = selectStmt.executeQuery();

            // Process the result
            if (resultSet.next()) {
                member_name = resultSet.getString("Name");
                member_email = resultSet.getString("Email");
                member_phone = resultSet.getString("Phone");

                System.out.println("Member Name: " + member_name);
                System.out.println("Member Email: " + member_email);
                System.out.println("Member Phone Number: " + member_phone);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
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
