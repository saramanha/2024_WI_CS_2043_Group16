package com.cpsis.gui;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIUtils {
	public static Object[][] filterDataByID(Object[][] originalData, int searchCriteria) {
		List<Object[]> filteredRows = new ArrayList<>();

        // Iterate through the original data and add rows with matching product ID to the filtered list
        for (Object[] row : originalData) {
            if (row.length > 0 && row[0] instanceof Integer) { //product ID is stored in the first column
                int id = (Integer) row[0];
                if (id == searchCriteria) {
                    filteredRows.add(row);
                    System.out.println(id);
                }
            }
        }

        // Convert the list of filtered rows to a 2D array
        return filteredRows.toArray(new Object[0][]);
    }

    public static Object[][] filterDataByName(Object[][] originalData, String searchCriteria) {
    	List<Object[]> filteredRows = new ArrayList<>();

    	for (Object[] row : originalData) {
            String productName = row[1].toString().toLowerCase(); // Assuming product name is in the 2nd column
            if (productName.contains(searchCriteria.toLowerCase())) {
                filteredRows.add(row);
            }
        }

        // Convert the list to a 2D array
        return filteredRows.toArray(new Object[0][]);
    }
	
	public static boolean isNumeric(String str) {
	    return str.matches("-?\\d+(\\.\\d+)?");
	}
	
    private static final String LOCATION_PATTERN = "\\d+-\\d+";
	public static boolean isValidLocation(String location) {
        Pattern pattern = Pattern.compile(LOCATION_PATTERN);
        Matcher matcher = pattern.matcher(location);
        return matcher.matches();
    }
    
    public static BigDecimal convertDiscountToBigDecimal(String discount) {
    	//Check if nothing has been entered in the discount field
    	if (discount == null || discount.trim().isEmpty()) {
            // Return a default discount value of BigDecimal.ZERO if input is empty
            return BigDecimal.ZERO;
        }
    	
    	try {
            // Parse the percentage string to an integer
            int discPercent = Integer.parseInt(discount);

            // Calculate the decimal representation of the percentage
            BigDecimal discDecimal = BigDecimal.valueOf(discPercent).divide(BigDecimal.valueOf(100));

            // Return the decimal representation
            return discDecimal;
        } catch (NumberFormatException e) {
            // Handle invalid input
            System.err.println("Invalid input: " + e.getMessage());
            return null;
        }
    }
}