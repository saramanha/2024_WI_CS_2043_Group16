package com.cpsis.gui;
import com.cpsis.database.*;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

public class TabbedDataTable {
	private static JTabbedPane tabbedPane;
	
    public static JPanel createTabbedDataTable() {
    	JPanel tabbedDataTablePanel = new JPanel();
    	tabbedDataTablePanel.setLayout(new BorderLayout());
    	tabbedDataTablePanel.setBorder(new EmptyBorder(0, 10, 0, 10)); // Add spacing on the sides of the table
    	
        tabbedPane = new JTabbedPane();
        
        Object[][] displayInvData = null;
        Object[][] stockInvData = null;
        Object[][] productData = null;
        Object[][] catData = null;
        Object[][] mfrData = null;
        
        try {
        	displayInvData = DatabaseManager.getDisplayInventory();
			stockInvData = DatabaseManager.getStockInventory();
			productData = DatabaseManager.getProductList();
			catData = DatabaseManager.getCategoryList();
			mfrData = DatabaseManager.getManufacturerList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        String[] displayInvTableCols = {"Display ID", "Product Name", " Display Quantity", "Price", "Display Location", "Expiration Date", "Discount"};
		createTableTab(tabbedPane, "Display Inventory", displayInvTableCols, displayInvData);
		
        String[] stockInvTableCols = {"Stock ID", "Product Name", "Stock Quantity", "Price", "Stock Location", "Expiration Date", "Discount"};
		createTableTab(tabbedPane, "Stock Inventory", stockInvTableCols, stockInvData);
		
    	String[] prodTableCols = {"Product ID", "Product Name", "Price", "Cat. Name", "Mfr. Name"};
		createTableTab(tabbedPane, "Product List", prodTableCols, productData);

		String[] catTableCols = {"Category ID", "Category Name"};
		createTableTab(tabbedPane, "Category List", catTableCols, catData);
		
		String[] mfrTableCols = {"Manufacturer ID", "Manufacturer Name"};
		createTableTab(tabbedPane, "Manufacturer List", mfrTableCols, mfrData);

		
        tabbedDataTablePanel.add(tabbedPane, BorderLayout.CENTER);
        return tabbedDataTablePanel;
    }
    
    
    private static void createTableTab(JTabbedPane tabbedPane, String tabName, String[] colNames, Object[][] data){
    	JTable newTableTab = new JTable(new DefaultTableModel(data, colNames));
        newTableTab.setDefaultEditor(Object.class, null); // Make the table non-editable
        JScrollPane scrollPane = new JScrollPane(newTableTab);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Set the preferred viewport size based on the number of rows
        int numRows = Math.min(newTableTab.getRowCount(), 10);
        newTableTab.setPreferredScrollableViewportSize(new Dimension(newTableTab.getPreferredScrollableViewportSize().width, newTableTab.getRowHeight() * numRows - 19));
        
        tabbedPane.addTab(tabName, scrollPane);
    }
    
    public static void updateTableTab(int tabIndex, Object[][] newData) {
    	if (tabIndex < 0 || tabIndex >= tabbedPane.getTabCount()) {
            return; // Invalid tab index
        }

    	// Get the existing scroll pane component from the tabbed pane
        Component tabComponent = tabbedPane.getComponentAt(tabIndex);
        if (tabComponent instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) tabComponent;
            Component viewport = scrollPane.getViewport().getView();
            if (viewport instanceof JTable) {
                JTable table = (JTable) viewport;
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                
                // Remove all existing rows from the table model
                model.setRowCount(0);

                // Add new rows to the table model
                for (Object[] row : newData) {
                    model.addRow(row);
                }
                
                // Recalculate the preferred viewport size based on the number of rows
                int numRows = Math.min(table.getRowCount(), 10);
                table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredScrollableViewportSize().width, table.getRowHeight() * numRows - 19));
                // Update the viewport size
                scrollPane.revalidate();
            }
        }
    }
}
