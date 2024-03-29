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
        String query = "INSERT INTO category VALUES(?)";
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
        String query = "INSERT INTO manufacturer VALUES(?)";
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
        String query = "INSERT INTO product VALUES(?,?,?,?)";
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
        String query = "INSERT INTO stock_inventory VALUES(?,?,?,?,?)";
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

    //Move product to the display inventory
    public static void moveProductToDisplayInv(int productId) throws SQLException {
        checkConnection(); // Ensure connection is established

        // Check whether product is already in display inventory
        String query;
        if(inInventory("display_inventory", productId))
            query = "UPDATE display_inventory SET quantity = quantity + 1 WHERE prod_id = ?";
        else
            query = "SELECT * INTO display_inventory FROM stock_inventory WHERE prod_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            //Set parameter
            statement.setObject(1, productId);

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

    //Helper method to check whether product with given ID is in a given table
    public static boolean inInventory(String dbName, int productId) throws SQLException {
        checkConnection(); // Ensure connection is established
        boolean inInv = false;
        String searchQuery = "SELECT COUNT(*) AS c FROM ? WHERE prod_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(searchQuery)){
            // Set parameters
            statement.setObject(1, dbName);
            statement.setObject(2, productId);

            //Get the amount of this product in the table
            ResultSet search = statement.executeQuery();
            int count = 0;
            while(search.next()){
                count = search.getInt("c");
            }

            //Decide whether product is in inventory
            if(count > 0)
                inInv = true;
        }
        return inInv;
    }

    public static List<SalesRecord> getSalesHistory() throws SQLException {
        checkConnection(); // Ensure connection is established
   
        String query = "SELECT * FROM sales_history"; // Replace 'sales_history' with your actual table name
        List<SalesRecord> salesHistory = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Retrieve each column value. 
                int id = resultSet.getInt("sale_id"); // Replace with your column name
                int productId = resultSet.getInt("prod_id"); // Replace with your column name
                int quantity = resultSet.getInt("sale_qty_sold"); // Replace with your column name
                double price = resultSet.getDouble("total_price"); // Replace with your column name
                Date saleDate = resultSet.getDate("sale_date"); // Replace with your column name

                //SalesRecord class with a constructor that matches these fields
                SalesRecord record = new SalesRecord(id, productId, quantity, price, saleDate);
                salesHistory.add(record);
            }
        }

        return salesHistory;
    }

    public static List<InventoryRecord> getDisplayInventory() throws SQLException {
        checkConnection(); // Ensure connection is established

        String query = "SELECT * FROM display_inventory";
        List<InventoryRecord> displayInv = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                //Retrieve each column value.
                int id = resultSet.getInt("display_id");
                int productId = resultSet.getInt("prod_id");
                int quantity = resultSet.getInt("display_qty");
                String location = resultSet.getString("display_loc");
                Date expDate = resultSet.getDate("display_exp");
                double discount = resultSet.getDouble("display_dis");

                // InventoryRecord class with a constructor matching these fields
                InventoryRecord record = new DisplayInvRecord(id, productId, quantity, location, expDate, discount);
                displayInv.add(record);
            }
        }
        
        return displayInv;
    }

    public static List<InventoryRecord> getStockInventory() throws SQLException {
        checkConnection(); // Ensure connection is established

        String query = "SELECT * FROM stock_inventory";
        List<InventoryRecord> stockInv = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                //Retrieve each column value.
                int id = resultSet.getInt("stock_id");
                int productId = resultSet.getInt("prod_id");
                int quantity = resultSet.getInt("stock_qty");
                String location = resultSet.getString("stock_loc");
                Date expDate = resultSet.getDate("stock_exp");
                double discount = resultSet.getDouble("stock_dis");

                // InventoryRecord class with a constructor matching these fields
                InventoryRecord record = new StockInvRecord(id, productId, quantity, location, expDate, discount);
                stockInv.add(record);
            }
        }
        
        return stockInv;
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
