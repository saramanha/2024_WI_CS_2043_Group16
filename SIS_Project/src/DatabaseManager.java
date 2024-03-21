import java.sql.*;

public class DatabaseManager{
    // JDBC URL for MySQL
    private static final String URL = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5693007";
    private static final String USERNAME = "sql5693007";
    private static final String PASSWORD = "fs2VEvY8hU";

    // Single database connection
    private static Connection connection = null;
    
    // Establish database connection
    public static void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
    }
    
    // Close database connection
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    // Insert product into the product database
    public static void addNewProduct(Product newProduct) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "insert into products values(?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters
            statement.setObject(1, newProduct.getName());
            statement.setObject(2, newProduct.getCategoryID());
            statement.setObject(3, newProduct.getManufacturerID());
            statement.setObject(4, newProduct.getPrice());

            // Execute the query
            statement.executeUpdate();
        }
    }
    
    // Retrieve data from the database
    public static ResultSet retrieveData() throws SQLException {
        checkConnection(); // Ensure connection is established
        PreparedStatement statement = connection.prepareStatement();
        return statement.executeQuery();
    }
}
