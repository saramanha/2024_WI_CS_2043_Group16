import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private Connection dbConnection;

    public void initializeConnection() {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Set up the connection parameters
            String url = "jdbc:mysql://localhost:3306/supermarket"; // DB name and server
            String user = "root"; //DB user
            String password = "password"; //DB password

            // Establish the connection to the database
            dbConnection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database established successfully.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

 
}