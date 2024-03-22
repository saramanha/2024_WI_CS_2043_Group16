import java.sql.Date;

public class SalesRecord {
    private int id;
    private int productId;
    private int quantitySold;
    private double totalCost;
    private Date saleDate;

    public SalesRecord(int id, int productId, int quantitySold, double totalCost, Date saleDate) {
        this.id = id;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.totalCost = totalCost;
        this.saleDate = saleDate;
    }

    // Getters and potentially setters
}

// ... Other methods to access SalesRecord data ...
