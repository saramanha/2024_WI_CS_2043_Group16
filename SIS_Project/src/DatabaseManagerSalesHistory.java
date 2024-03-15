import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManagerSalesHistory {

    private Connection dbConnection;

    // Assuming the initializeConnection method is implemented here

    public List<SalesRecord> getSalesHistory() {
        List<SalesRecord> salesHistory = new ArrayList<>();
        String query = "SELECT * FROM sales"; // Replace 'sales' with whatever  table name for sales records is

        try (PreparedStatement statement = dbConnection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int saleID = resultSet.getInt("SaleID"); // column name for the sale ID
                int productID = resultSet.getInt("ProductID"); //  column name for the product ID
                int quantitySold = resultSet.getInt("QuantitySold"); 
                double totalCost = resultSet.getDouble("TotalCost");
                java.sql.Date saleDate = resultSet.getDate("SaleDate");

                SalesRecord salesRecord = new SalesRecord(saleID, productID, quantitySold, totalCost, saleDate);
              
                
                salesHistory.add(salesRecord);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesHistory;
    }

}


class SalesRecord {
    private int saleID;
    private int productID;
    private int quantitySold;
    private double totalCost;
    private java.sql.Date saleDate;

    public SalesRecord(int saleID, int productID, int quantitySold, double totalCost, java.sql.Date saleDate) {
        this.saleID = saleID;
        this.productID = productID;
        this.quantitySold = quantitySold;
        this.totalCost = totalCost;
        this.saleDate = saleDate;
    }

    // getters and setters
}