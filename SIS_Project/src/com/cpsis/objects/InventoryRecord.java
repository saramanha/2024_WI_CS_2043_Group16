package com.cpsis.objects;
import java.math.BigDecimal;
import java.time.LocalDate;

public class InventoryRecord {
	private int productID;
	private int quantity;
	private String location;
	private LocalDate expirationDate;
	private BigDecimal discount;
	
	public InventoryRecord(int prodID, int qty, String loc, LocalDate expDate, BigDecimal disc) {
		productID = prodID;
		quantity = qty;
		location = loc;
		expirationDate = expDate;
		discount = disc;
	}
	
	public int getProductID() {
		return productID;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public String getLocation() {
		return location;
	}
	
	public LocalDate getExpiration() {
		return expirationDate;
	}
	
	public BigDecimal getDiscount() {
		return discount;
	}
}