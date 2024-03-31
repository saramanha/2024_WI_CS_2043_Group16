package com.cpsis.objects;
import java.math.BigDecimal;

public class Product {
	private String productName;
	private int categoryID;
	private int manufacturerID;
	private BigDecimal productPrice;
	
	public Product(String prodName, int catID, int mfrID, BigDecimal price) {
		productName = prodName;
		categoryID = catID;
		manufacturerID = mfrID;
		productPrice = price;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public int getCategoryID() {
		return categoryID;
	}
	
	public int getManufacturerID() {
		return manufacturerID;
	}
	
	public BigDecimal getProductPrice() {
		return productPrice;
	}
}
