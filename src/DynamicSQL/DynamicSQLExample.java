package DynamicSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
Dynamic SQL: Definition, Features, and Use Cases
Dynamic SQL refers to SQL statements that are constructed and executed at runtime, offering more flexibility and adaptability compared to static SQL.
Instead of hardcoding SQL queries, dynamic SQL allows programs to build SQL queries dynamically based on input parameters, user interactions, or specific application logic.
This approach enables handling varying query structures and more complex scenarios that are difficult to achieve with static SQL alone.

Key Features of Dynamic SQL
    1.Runtime Query Generation: SQL statements are generated at runtime, allowing the system to adjust the queries based on user input, application logic, or data state.
    2.Flexibility: Dynamic SQL enables applications to execute different types of queries (e.g., SELECT, INSERT, UPDATE, or DELETE) without predefined, hardcoded query statements.
    3.Handling Complex Queries: Dynamic SQL is useful when queries are too complex or conditional for static SQL to handle efficiently, especially when working with optional parameters or variable WHERE clauses.
    4.Adaptability: With dynamic SQL, an application can change the structure of the SQL query depending on data or circumstances, such as filtering or sorting results based on user-specified criteria.
    5.Potential Security Risks: Dynamic SQL can lead to SQL injection vulnerabilities if not properly handled. Using prepared statements or parameterized queries helps mitigate these risks.

Use Cases for Dynamic SQL
    1.Building Complex Queries: When queries involve optional conditions or filters, dynamic SQL allows flexibility in constructing these queries.
    2.Handling User-Defined Inputs: Applications that need to adjust SQL queries based on various user inputs, such as search conditions or filtering criteria, often rely on dynamic SQL.
    3.Batch Processing: Dynamic SQL can be used for creating and executing multiple queries dynamically, especially in scenarios where different database operations need to be performed based on runtime data.

 */
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
