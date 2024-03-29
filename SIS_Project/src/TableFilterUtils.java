import java.util.ArrayList;
import java.util.List;

public class TableFilterUtils {
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
}
