package com.cpsis.database;
import com.cpsis.objects.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

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
    public static void moveProductToDisplayInv(int stockID, int productId, int quantity, String displayLoc, Date exipiration, BigDecimal discount) throws SQLException {
    	checkConnection(); // Ensure connection is established
        String query;
        // Check whether product is already in display inventory table
        int displayID = inDisplayTable(productId, exipiration);
        if(displayID > 0)
            query = "UPDATE DISPLAY_INVENTORY " +
            		"SET DISPLAY_QTY = DISPLAY_QTY + ?, DISPLAY_LOC = ?, DISPLAY_DIS = ? " + 
            		"WHERE DISPLAY_ID = ?";
        else
            query = "INSERT INTO DISPLAY_INVENTORY (PROD_ID, DISPLAY_QTY, DISPLAY_LOC, DISPLAY_EXP, DISPLAY_DIS) " +
                    "SELECT PROD_ID, ?, ?, STOCK_EXP, ? FROM STOCK_INVENTORY WHERE STOCK_ID = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
        	statement.setInt(1, quantity);
        	statement.setString(2, displayLoc);
        	statement.setBigDecimal(3, discount);
        	if(displayID > 0) {
            	statement.setInt(4, displayID);
            }
            else {
            	statement.setInt(4, stockID);
            }

            // Execute the query
            statement.executeUpdate();
            decInvQty("STOCK_INVENTORY", "STOCK_ID", "STOCK_QTY", stockID, quantity);
        }
    }

    //Helper method to check whether product with given ID (and expiration date if given) is in a given table
    public static int inDisplayTable(int productId, Date expiration) throws SQLException {
        checkConnection(); // Ensure connection is established
        int resultID = 0;
        String query = "SELECT * FROM DISPLAY_INVENTORY WHERE PROD_ID = ?";
        if(expiration != null) {
        	query += " AND DISPLAY_EXP = ?";
        }
        try (PreparedStatement statement = connection.prepareStatement(query)){
            // Set parameters
            statement.setObject(1, productId);
            if(expiration != null) {
            	statement.setObject(2, expiration);
            }
            //Get the ID for result
            ResultSet search = statement.executeQuery();
            if(search.next()){
                resultID = search.getInt(1);
            }
        }
        return resultID;
    }
    
    public static void removeProductFromDisplayInventory(SalesRecord newSalesRecord, int displayID) throws SQLException {
    	checkConnection(); // Ensure connection is established
        String query;
        int qtySold = newSalesRecord.getQtySold();
        int productId = newSalesRecord.getProductID();
        Date dateSold = Date.valueOf(newSalesRecord.getSaleDate());
        BigDecimal saleDiscount = newSalesRecord.getSaleDiscount();
        // Check whether product is already in sales history table
        int saleID = inSalesHisTable(productId, dateSold, saleDiscount);
        if(saleID > 0)
            query = "UPDATE SALES_HISTORY " +
            		"SET QTY_SOLD = QTY_SOLD + ? " + 
            		"WHERE SALE_ID = ?";
        else
            query = "INSERT INTO SALES_HISTORY (QTY_SOLD, PROD_ID, SALE_DIS, DATE_SOLD) VALUES(?,?,?,?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
        	statement.setInt(1, qtySold);
        	if(saleID > 0) {
            	statement.setInt(2, saleID);
            }
            else {
            	statement.setInt(2, productId);
            	statement.setBigDecimal(3, saleDiscount);
            	statement.setDate(4, dateSold);
            }
            // Execute the query
            statement.executeUpdate();
            decInvQty("DISPLAY_INVENTORY", "DISPLAY_ID", "DISPLAY_QTY", displayID, qtySold);
        }
    }
    
    public static int inSalesHisTable(int prodID, Date dateSold, BigDecimal discount) throws SQLException{
    	checkConnection(); // Ensure connection is established
        int resultID = 0;
        String query = "SELECT * FROM DISPLAY_INVENTORY WHERE PROD_ID = ? AND DATE_SOLD = ? AND SALE_DIS = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            // Set parameters
            statement.setInt(1, prodID);
            statement.setDate(2, dateSold);
            statement.setBigDecimal(3, discount);
            //Get the ID for result
            ResultSet search = statement.executeQuery();
            if(search.next()){
                resultID = search.getInt(1);
            }
        }
        return resultID;
    }
    
    public static void decInvQty(String tableName, String colName, String qtyColName, int invID, int quantity) throws SQLException {
    	checkConnection(); // Ensure connection is established
    	String query = "UPDATE " + tableName + " SET " + qtyColName + " = " + qtyColName + " - ? WHERE " + colName + " = ?";
    	try (PreparedStatement statement = connection.prepareStatement(query)) {
    		statement.setObject(1, quantity);
    		statement.setObject(2, invID);
    		// Execute the update query
            int rowsUpdated = statement.executeUpdate();
            
            if (rowsUpdated > 0) {
                // The update was successful, retrieve the resulting quantity and if = 0 remove record
                String selectQuery = "SELECT STOCK_QTY FROM STOCK_INVENTORY WHERE STOCK_ID = ?";
                try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                    selectStatement.setInt(1, invID);
                    try (ResultSet resultSet = selectStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int resultQty =  resultSet.getInt("STOCK_QTY"); // Return the resulting STOCK_QTY
                            if(resultQty == 0) {
                            	deleteInvRecord(tableName, invID, colName);
                            }
                        }
                    }
                }
            }
    	}
    }
    
    public static void deleteInvRecord(String tableName, int invID, String colName) throws SQLException{
    	checkConnection(); // Ensure connection is established
    	String query = "DELETE FROM " + tableName + " WHERE " + colName + " = ?";
    	try (PreparedStatement statement = connection.prepareStatement(query)) {
    		statement.setObject(1, invID);
    		// Execute the query
            statement.executeUpdate();
    	}
    }

    public static Object[][] getDisplayInventory() throws SQLException {
        checkConnection(); // Ensure connection is established
 
        String query = "SELECT D.DISPLAY_ID, P.PROD_NAME, D.DISPLAY_QTY, P.PROD_PRICE, D.DISPLAY_LOC, D.DISPLAY_EXP, D.DISPLAY_DIS " + 
        			   "FROM DISPLAY_INVENTORY D " + 
        			   "JOIN PRODUCT P ON D.PROD_ID = P.PROD_ID";
        try (PreparedStatement statement = connection.prepareStatement(query);
           	 ResultSet resultSet = statement.executeQuery()) {
           	//Convert resultSet to Object[][] and return it 
               return resultSetToObjectArr(resultSet);
               
        } catch (SQLException e) {
        	e.printStackTrace(); // Print the exception details to the console for debugging
        	System.out.println("Error querying database for display inventory data");
        	return new Object[0][]; // Return an empty array in case of error
        }
    }
 
    public static Object[][] getStockInventory() throws SQLException {
        checkConnection(); // Ensure connection is established
 
        String query = "SELECT S.STOCK_ID, P.PROD_NAME, S.STOCK_QTY, P.PROD_PRICE, S.STOCK_LOC, S.STOCK_EXP, S.STOCK_DIS " +
        				"FROM STOCK_INVENTORY S " +
        				"JOIN PRODUCT P ON S.PROD_ID = P.PROD_ID";
 
        try (PreparedStatement statement = connection.prepareStatement(query);
           	 ResultSet resultSet = statement.executeQuery()) {
           	   //Convert resultSet to Object[][] and return it 
               return resultSetToObjectArr(resultSet);
               
           } catch (SQLException e) {
               e.printStackTrace(); // Print the exception details to the console for debugging
               System.out.println("Error querying database for stock inventory data");
               return new Object[0][]; // Return an empty array in case of error
           }
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
    
    public static List<String> getSalesHistory() throws SQLException {
        checkConnection();
    	List<String> salesHistoryData = new ArrayList<>();
    	
    	String query = "SELECT S.SALE_ID, S.DATE_SOLD, P.PROD_ID, P.PROD_NAME, S.QTY_SOLD, P.PROD_PRICE, S.SALE_DIS * 100.0 AS SALE_DIS, " +
		    		   "S.QTY_SOLD * (P.PROD_PRICE * S.SALE_DIS) AS TOTAL_AMOUNT " +
		    		   "FROM SALES_HISTORY S " +
    				   "JOIN PRODUCT P ON P.PROD_ID = S.PROD_ID";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int saleId = resultSet.getInt("SALE_ID");
                Date dateSold = resultSet.getDate("DATE_SOLD");
                int productId = resultSet.getInt("PROD_ID");
                String productName = resultSet.getString("PROD_NAME");
                int quantitySold = resultSet.getInt("QTY_SOLD");
                BigDecimal productPrice = resultSet.getBigDecimal("PROD_PRICE");
                BigDecimal saleDiscount = resultSet.getBigDecimal("SALE_DIS");
                BigDecimal totalAmount = resultSet.getBigDecimal("TOTAL_AMOUNT");
                
                // Format the dateSold using SimpleDateFormat
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateSoldStr = dateFormat.format(dateSold);

                // Format the BigDecimal values
                String productPriceStr = productPrice.toString(); // Convert to string
                String saleDiscountStr = saleDiscount.toString(); // Convert to string
                String totalAmountStr = totalAmount.toString(); // Convert to string

                // Construct the sales record string
                String salesRecord = String.format("%d %s %d %s %d $%s %s%% $%s",
                        saleId, dateSoldStr, productId, productName, quantitySold, productPriceStr, saleDiscountStr, totalAmountStr);

                salesHistoryData.add(salesRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately (e.g., log, throw, etc.)
        }

        return salesHistoryData;
    }
    
    public static Object[][] getCategoryList() throws SQLException {
    	checkConnection(); // Ensure connection is established
    	
    	String query = "SELECT CAT_ID, CAT_NAME FROM CATEGORY";
    	
    	try (PreparedStatement statement = connection.prepareStatement(query);
    		ResultSet resultSet = statement.executeQuery()) {
    		//Convert resultSet to Object[][] and return it 
    		return resultSetToObjectArr(resultSet);
               
    	} catch (SQLException e) {
    		e.printStackTrace(); // Print the exception details to the console for debugging
    		System.out.println("Error querying database for category data");
    		return new Object[0][]; // Return an empty array in case of error
    	}
    }
    
    public static Object[][] getManufacturerList() throws SQLException {
    	checkConnection(); // Ensure connection is established
    	
    	String query = "SELECT MFR_ID, MFR_NAME FROM MANUFACTURER";
    	
    	try (PreparedStatement statement = connection.prepareStatement(query);
    		ResultSet resultSet = statement.executeQuery()) {
    		//Convert resultSet to Object[][] and return it 
    		return resultSetToObjectArr(resultSet);
               
    	} catch (SQLException e) {
    		e.printStackTrace(); // Print the exception details to the console for debugging
    		System.out.println("Error querying database for manufacturer data");
    		return new Object[0][]; // Return an empty array in case of error
    	}
    }
    
    public static int getProdIDFromInvID(int invID, String tableName, String colName) throws SQLException{
    	checkConnection();
    	String query = "SELECT PROD_ID FROM " + tableName + " WHERE " + colName + " = ?";
    	int resultID = 0;
    	try (PreparedStatement statement = connection.prepareStatement(query)) {
        	statement.setInt(1, invID);
        	ResultSet result = statement.executeQuery();
        	if (result.next()) {
        	    resultID = result.getInt(1); // Retrieves the value from the first column
        	}
    	}
    	return resultID;
    }
    
    public static void updateDiscount(BigDecimal newDiscount, int stockID) throws SQLException {
    	checkConnection();
        String query = "UPDATE STOCK_INVENTORY SET STOCK_DIS = ? WHERE STOCK_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBigDecimal(1, newDiscount);
            statement.setInt(2, stockID);
            statement.executeUpdate();
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