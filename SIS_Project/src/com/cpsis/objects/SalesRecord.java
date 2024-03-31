package com.cpsis.objects;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesRecord {
    private int productId;
    private int quantitySold;
    private BigDecimal saleDiscount;
    private LocalDate saleDate;

    public SalesRecord(int prodId, int qtySold, BigDecimal discount, LocalDate date) {
        productId = prodId;
        quantitySold = qtySold;
        saleDiscount = discount;
        saleDate = date;
    }
    
    public int getProductID() {
    	return productId;
    }
    
    public int getQtySold() {
    	return quantitySold;
    }

    public BigDecimal getSaleDiscount() {
    	return saleDiscount;
    }
    
    public LocalDate getSaleDate() {
    	return saleDate;
    }
}