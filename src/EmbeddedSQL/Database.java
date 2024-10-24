package EmbeddedSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    public static void main(String[] args) {
        try {
            // Correct SQL query to select a member's name
            String mySQL = "SELECT name FROM members WHERE MemberID=1";
            String url = "jdbc:mysql://localhost:3306/library_system";  // Correct the JDBC URL
            String user = "root";
            String password = "Cyril@2019";

            // 1. Create connection
            Connection con = DriverManager.getConnection(url, user, password);

            // 2. Create a statement/query
            Statement stmt = con.createStatement();
            String str = "INSERT INTO members VALUES (3, 'Godfrey', 'gouma@myusername.org', '0723456790')";  // Phone number as a string

            // 3. Execute the statement
            stmt.execute(str);

            // 4. Close connection
            con.close();
            System.out.println("Query executed successfully.");

        } catch (Exception e) {
            e.printStackTrace();  // Print the stack trace for debugging
        }
    }
}
