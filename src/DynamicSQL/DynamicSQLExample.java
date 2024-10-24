package DynamicSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DynamicSQLExample {

    public static void main(String[] args) {
        // User input variables (can be null if not used)
        String filterName = "Alice Brown";  // Change to null if not filtering by name
        String filterEmail = "alice.brown@example.com";  // Change to null if not filtering by email

        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/library_system"; // Replace with your database name
        String dbUser = "root"; // Replace with your MySQL username
        String dbPassword = "Cyril@2019"; // Replace with your MySQL password

        Connection connection = null;

        try {
            // Establish connection to the database
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Start building the dynamic SQL query
            StringBuilder queryBuilder = new StringBuilder("SELECT MemberID, Name, Email, Phone FROM members WHERE 1=1");

            // Add conditions dynamically based on input
            if (filterName != null && !filterName.isEmpty()) {
                queryBuilder.append(" AND Name = ?");
            }
            if (filterEmail != null && !filterEmail.isEmpty()) {
                queryBuilder.append(" AND Email = ?");
            }

            // Prepare the statement
            PreparedStatement pstmt = connection.prepareStatement(queryBuilder.toString());

            // Set parameters dynamically
            int paramIndex = 1;
            if (filterName != null && !filterName.isEmpty()) {
                pstmt.setString(paramIndex++, filterName);
            }
            if (filterEmail != null && !filterEmail.isEmpty()) {
                pstmt.setString(paramIndex++, filterEmail);
            }

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // Process the result set
            while (rs.next()) {
                int memberId = rs.getInt("MemberID");
                String memberName = rs.getString("Name");
                String memberEmail = rs.getString("Email");
                String memberPhone = rs.getString("Phone");

                System.out.println("Member ID: " + memberId);
                System.out.println("Name: " + memberName);
                System.out.println("Email: " + memberEmail);
                System.out.println("Phone: " + memberPhone);
                System.out.println("---------------------------");
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
