import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

public class tabbedDataTable {
    public static JPanel createTabbedDataTable(Object[][][] data) {
    	JPanel tabbedDataTablePanel = new JPanel();
    	tabbedDataTablePanel.setLayout(new BorderLayout());
    	tabbedDataTablePanel.setBorder(new EmptyBorder(0, 10, 0, 10)); // Add spacing on the sides of the table
    	
        JTabbedPane tabbedPane = new JTabbedPane();

        createInventoryTable(tabbedPane, data, "Display ");
        createInventoryTable(tabbedPane, data, "Stock ");
        createProductTable(tabbedPane, data);
        createCategoryTable(tabbedPane, data);
        createManufacturerTable(tabbedPane, data);
        
        tabbedDataTablePanel.add(tabbedPane, BorderLayout.CENTER);
        return tabbedDataTablePanel;
    }
    
    
    private static void createInventoryTable(JTabbedPane tabbedPane, Object[][][] data, String tabName){
    	Object[] invTableColNames = {tabName + "ID", "Product Name", "Quantity", "Price", "Location", "Expiration Date", "Discount"};
    	//Object[][] data = //method from DatabaseManager that does join to get the correct information
    	JTable inventoryTable = new JTable(new DefaultTableModel(data[0], invTableColNames));
    	inventoryTable.setDefaultEditor(Object.class, null); // Make the table non-editable
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        tabbedPane.addTab(tabName + "Inventory", scrollPane);
        
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    private static void createProductTable(JTabbedPane tabbedPane, Object[][][] data){
    	Object[] prodTableColNames = {"Product ID", "Product Name", "Price", "Cat. Name", "Mfr. Name"};
    	//Object[][] data = //method from DatabaseManager that does join to get the correct information
    	JTable prodTable = new JTable(new DefaultTableModel(data[1], prodTableColNames));
    	prodTable.setDefaultEditor(Object.class, null); // Make the table non-editable
        JScrollPane scrollPane = new JScrollPane(prodTable);
        tabbedPane.addTab("Product List", scrollPane);
    
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    private static void createCategoryTable(JTabbedPane tabbedPane, Object[][][] data){
    	Object[] catTableColNames = {"Category ID", "Category Name"};
    	//Object[][] data = //method from DatabaseManager that does join to get the correct information
    	JTable catTable = new JTable(new DefaultTableModel(data[2], catTableColNames));
    	catTable.setDefaultEditor(Object.class, null); // Make the table non-editable
        JScrollPane scrollPane = new JScrollPane(catTable);
        tabbedPane.addTab("Category List", scrollPane);
    
        int numRows = Math.min(catTable.getRowCount(), 10);
        catTable.setPreferredScrollableViewportSize(new Dimension(catTable.getPreferredScrollableViewportSize().width, catTable.getRowHeight() * numRows));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    private static void createManufacturerTable(JTabbedPane tabbedPane, Object[][][] data){
    	Object[] mfrTableColNames = {"Manufacturer ID", "Manufacturer Name"};
    	//Object[][] data = //method from DatabaseManager that does join to get the correct information
    	JTable mfrTable = new JTable(new DefaultTableModel(data[2], mfrTableColNames));
    	mfrTable.setDefaultEditor(Object.class, null); // Make the table non-editable
        JScrollPane scrollPane = new JScrollPane(mfrTable);
        tabbedPane.addTab("Manufacturer List", scrollPane);
    
        int numRows = Math.min(mfrTable.getRowCount(), 10);
        mfrTable.setPreferredScrollableViewportSize(new Dimension(mfrTable.getPreferredScrollableViewportSize().width, mfrTable.getRowHeight() * numRows));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
}
