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

    public static List<SalesRecord> getSalesHistory() throws SQLException {
    checkConnection(); // Ensure connection is established

   
    String query = "SELECT * FROM sales_history"; // Replace 'sales_history' with your actual table name
    List<SalesRecord> salesHistory = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {
            // Retrieve each column value. 
            int id = resultSet.getInt("id"); // Replace with your column name
            int productId = resultSet.getInt("product_id"); // Replace with your column name
            int quantity = resultSet.getInt("quantity_sold"); // Replace with your column name
            double price = resultSet.getDouble("total_price"); // Replace with your column name
            Date saleDate = resultSet.getDate("sale_date"); // Replace with your column name

            //SalesRecord class with a constructor that matches these fields
            SalesRecord record = new SalesRecord(id, productId, quantity, price, saleDate);
            salesHistory.add(record);
        }
    }

    return salesHistory;
    }




    public static void removeProductFromDisplayInventory(int productId) throws SQLException {
        checkConnection(); // Ensure the connection is established

        // SQL query to delete a product from the display inventory table by its product ID
        String query = "DELETE FROM display_inventory WHERE product_id = ?"; // Replace 'display_inventory' and 'product_id' with your actual table and column names

        try (PreparedStatement statement = connection.prepareStatement(query)) {
        // Set the product ID parameter in the query
        statement.setInt(1, productId);

        // Execute the delete statement
        int rowsAffected = statement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Product removed from display inventory successfully.");
        } else {
            System.out.println("No product found with the provided ID.");
        }
        }
    }




}
