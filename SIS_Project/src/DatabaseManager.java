import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static int addNewCategory(Category c) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "INSERT INTO CATEGORY (CAT_NAME) VALUE(?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            statement.setObject(1, c.getName());

            // Execute the query
            statement.executeUpdate();
            
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey != null && generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve auto-generated ID.");
            }
        }
    }

    //Add a new manufacturer
    public static int addNewManufacturer(Manufacturer m) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "INSERT INTO MANUFACTURER (MFR_NAME) VALUE(?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            statement.setObject(1, m.getName());

            // Execute the query
            statement.executeUpdate();
            
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey != null && generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve auto-generated ID.");
            }
        }
    }

    // Insert product into the product database
    public static int addNewProduct(Product newProduct) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "INSERT INTO PRODUCT (PROD_NAME, PROD_PRICE, CAT_ID, MFR_ID) VALUES(?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            statement.setObject(1, newProduct.getProductName());
            statement.setObject(2, newProduct.getProductPrice());
            statement.setObject(3, newProduct.getCategoryID());
            statement.setObject(4, newProduct.getManufacturerID());

            // Execute the query
            statement.executeUpdate();
            
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey != null && generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve auto-generated ID.");
            }
        }
    }

    //Add product to the stock inventory
    public static void addProductToStockInv(InventoryRecord record) throws SQLException {
        checkConnection(); // Ensure connection is established
        String query = "INSERT INTO STOCK_INVENTORY (PROD_ID, STOCK_QTY, STOCK_LOC, STOCK_EXP, STOCK_DIS) VALUES(?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters
            statement.setObject(1, record.getProductID());
            statement.setObject(2, record.getQuantity());
            statement.setObject(3, record.getLocation());
            if (record.getExpiration() != null) {
                statement.setObject(4, Date.valueOf(record.getExpiration()));
            } else {
                statement.setObject(4, null); // Set null if expiration date is null
            }
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
    
    public static Object[][] getProductList() throws SQLException {
        checkConnection(); // Ensure connection is established

        String query = "SELECT P.PROD_ID, P.PROD_NAME, P.PROD_PRICE, C.CAT_NAME, M.MFR_NAME " +
                	   "FROM PRODUCT P " +
                	   "JOIN CATEGORY C ON P.CAT_ID = C.CAT_ID " +
                	   "JOIN MANUFACTURER M ON P.MFR_ID = M.MFR_ID";

        try (PreparedStatement statement = connection.prepareStatement(query);
        	 ResultSet resultSet = statement.executeQuery()) {
        	//Convert resultSet to Object[][] and return it 
            return resultSetToObjectArr(resultSet);
            
        } catch (SQLException e) {
            e.printStackTrace(); // Print the exception details to the console for debugging
            System.out.println("Error querying database for Product data");
            return new Object[0][]; // Return an empty array in case of error
        }
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
    
    public static Map<String, Integer> getCategories() throws SQLException {
    	checkConnection(); // Ensure the connection is established
    	Map<String, Integer> categoriesMap = new HashMap<>();
        Statement statement = null;
        ResultSet resultSet = null;
    	String query = "SELECT CAT_ID, CAT_NAME FROM CATEGORY";
    	
    	try {
    		statement = connection.createStatement(); 
    		resultSet = statement.executeQuery(query);
    		
    		while (resultSet.next()) {
                int categoryId = resultSet.getInt("CAT_ID");
                String categoryName = resultSet.getString("CAT_NAME");
                categoriesMap.put(categoryName, categoryId);
            }
    	 } finally {
			// Close resources
			if (resultSet != null) resultSet.close();
			if (statement != null) statement.close();
			if (connection != null) connection.close();
    	 }

    	 return categoriesMap;	
	}
        
    public static Map<String, Integer> getManufacturers() throws SQLException {
    	checkConnection(); // Ensure the connection is established
    	Map<String, Integer> manufacturerMap = new HashMap<>();
        Statement statement = null;
        ResultSet resultSet = null;
    	String query = "SELECT MFR_ID, MFR_NAME FROM MANUFACTURER";
    	
    	try {
    		statement = connection.createStatement(); 
    		resultSet = statement.executeQuery(query);
    		
    		while (resultSet.next()) {
                int manufacturerId = resultSet.getInt("MFR_ID");
                String manufacturerName = resultSet.getString("MFR_NAME");
                manufacturerMap.put(manufacturerName, manufacturerId);
            }
    	 } finally {
			// Close resources
			if (resultSet != null) resultSet.close();
			if (statement != null) statement.close();
			if (connection != null) connection.close();
    	 }

    	 return manufacturerMap;	
	}

    private static Object[][] resultSetToObjectArr(ResultSet resultSet) throws SQLException {
		int numColumns = resultSet.getMetaData().getColumnCount();
		List<Object[]> rows = new ArrayList<>();
		
		while (resultSet.next()) {
		    Object[] row = new Object[numColumns];
		    for (int i = 1; i <= numColumns; i++) {
		        row[i - 1] = resultSet.getObject(i);
		    }
		    rows.add(row);
		}
		
		Object[][] data = new Object[rows.size()][];
		for (int i = 0; i < rows.size(); i++) {
		    data[i] = rows.get(i);
		}
		
		return data;
	}
}
