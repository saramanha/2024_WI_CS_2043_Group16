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
    
    //Add new category
    public static void addNewCategory(Category c) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "insert into category values(?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters
            statement.setObject(1, c.getName());

            // Execute the query
            statement.executeUpdate();
        }
    }

    //Add a new manufacturer
    public static void addNewManufacturer(Manufacturer m) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "insert into manufacturer values(?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters
            statement.setObject(1, m.getName());

            // Execute the query
            statement.executeUpdate();
        }
    }

    // Insert product into the product database
    public static void addNewProduct(Product newProduct) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "insert into product values(?,?,?,?)";
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

    //Add product to the stock inventory
    public static void addProductToStockInv(InventoryRecord record) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "insert into stock_inventory values(?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters
            statement.setObject(1, record.getProductID());
            statement.setObject(2, record.getQuantity());
            statement.setObject(3, record.getLocation());
            statement.setObject(4, record.getExpiration());
            statement.setObject(5, record.getDiscount());

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