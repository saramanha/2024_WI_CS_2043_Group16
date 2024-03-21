import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testClass1 {
	public static void main(String[] args) {
        // JDBC URL for MySQL
        String url = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5693007";
        String username = "sql5693007";
        String password = "fs2VEvY8hU";

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // Connection successful
            System.out.println("Connected to the database.");

            // Use the connection for database operations
            // For example: execute queries, update data, etc.

            // Close the connection when done
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

}