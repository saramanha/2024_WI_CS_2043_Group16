package com.cpsis.gui;
import com.cpsis.database.DatabaseManager;
import com.cpsis.objects.SalesRecord;

import java.sql.*;
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class RemoveFromDisplayClicked {
    private Object[][] data;
    private JTable resultTable;
    private int displayID;
    private int productID;
    private int inputQty;
    private int displayQty;
    private BigDecimal discount;
    
    public RemoveFromDisplayClicked() {
        JFrame removeFrame = new JFrame();
        removeFrame.setTitle("Remove Product from Display Inventory");
        removeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        removeFrame.setContentPane(contentPane);


         // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton revertButton = new JButton("Remove Filter");
		revertButton.setVisible(false);
        searchPanel.add(new JLabel("Search by Name or ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
		searchPanel.add(revertButton);

		try {
			data = DatabaseManager.getDisplayInventory();
		} catch (SQLException e) {
			System.out.println("Error getting display inventory data");
			e.printStackTrace();
		}
		
        // Define the column names for the table model
        String[] columnNames = {"Display ID", "Product Name", "Display Quantity", "Price", "Display Location", "Expiration Date", "Discount"};
        resultTable = new JTable(data, columnNames);
		resultTable.setDefaultEditor(Object.class, null); // Make the table non-editable

        JScrollPane scrollPane = new JScrollPane(resultTable);
        JPanel tablePanel = new JPanel(new BorderLayout()); // Wrap the scroll pane in a panel
        tablePanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add padding around the table
        tablePanel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the panel
        
        // Set the maximum number of visible rows for the table
        int numRows = Math.min(resultTable.getRowCount(), 10);
        resultTable.setPreferredScrollableViewportSize(new Dimension(resultTable.getPreferredScrollableViewportSize().width, resultTable.getRowHeight() * numRows));

        tablePanel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the panel

        // Additional Fields Panel
        JPanel additionalFieldsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Align components to the right
        JTextField quantityField = new JTextField(10);
        JButton submitButton = new JButton("Submit");
        additionalFieldsPanel.add(new JLabel("Quantity:"));
        additionalFieldsPanel.add(quantityField);
        additionalFieldsPanel.add(Box.createHorizontalStrut(50)); // Add horizontal spacing of 10 pixels
        additionalFieldsPanel.add(submitButton);

        //Example of using the slected row of the datatable
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//Getting the selected row as input
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) { // If a row is selected
                	displayID = (Integer) resultTable.getValueAt(selectedRow, 0);
                	displayQty = (Integer) resultTable.getValueAt(selectedRow, 2);
                	discount = (BigDecimal) resultTable.getValueAt(selectedRow, 6);
                } else {
                    JOptionPane.showMessageDialog(removeFrame, "Please select a row.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
                }
                
                try {
					productID = DatabaseManager.getProdIDFromInvID(displayID, "DISPLAY_INVENTORY", "DISPLAY_ID");
				} catch (SQLException e1) {
					System.out.println("Failed to retreive Product ID using Display ID");
					e1.printStackTrace();
				}
                
                //Getting the quantity input
                try {
            	    inputQty = Integer.parseInt(quantityField.getText());
            	} catch (NumberFormatException e1) {
            		JOptionPane.showMessageDialog(removeFrame, "Invalid quantity. Please use numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
                if(inputQty > displayQty) {
                	JOptionPane.showMessageDialog(removeFrame, "Invalid quantity. Please ensure there is enough in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                	return;
                }
                
                LocalDate currentData = LocalDate.now();
                SalesRecord newSalesRecord = new SalesRecord(productID, inputQty, discount, currentData);
                try {
					DatabaseManager.removeProductFromDisplayInventory(newSalesRecord, displayID);
				} catch (SQLException e1) {
					System.out.println("Failed to remove product to display inventory");
					e1.printStackTrace();
				}
                
                //Update tables
                try {
        			Object[][] displayData = DatabaseManager.getDisplayInventory();
            		TabbedDataTable.updateTableTab(0, displayData);
                } catch (SQLException e1) {
        			System.out.println("Error getting display inventory data");
        			e1.printStackTrace();
        		}

                //Close window once everything is processed and database is updated
                removeFrame.dispose();
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the search criteria from the text field
	            String searchCriteria = searchField.getText().trim();
	            Object[][] filteredData;
	            
	            if (searchCriteria.isEmpty()) {
	            	return;
	            }
	            // Check if the search criteria is a number
	            else if (GUIUtils.isNumeric(searchCriteria)) {
	                // Filter by product ID (1st column)
	                filteredData = GUIUtils.filterDataByID(data, Integer.parseInt(searchCriteria));
	                revertButton.setVisible(true);
	            } else {
	                // Filter by product name (2nd column)
	            	filteredData = GUIUtils.filterDataByName(data, searchCriteria);
	            	revertButton.setVisible(true);
	            }

	            // Update the table with the filtered data
	            DefaultTableModel model = new DefaultTableModel(filteredData, columnNames);
	            resultTable.setModel(model);
			}
		});
		
		revertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = new DefaultTableModel(data, columnNames);
				resultTable.setModel(model);
				revertButton.setVisible(false);
			}
		});
		
        // Add components to the content pane
        contentPane.add(searchPanel, BorderLayout.NORTH);
        contentPane.add(tablePanel, BorderLayout.CENTER);
        contentPane.add(additionalFieldsPanel, BorderLayout.SOUTH);

        removeFrame.pack();
        removeFrame.setMinimumSize(new Dimension(750, 0));
        removeFrame.setLocationRelativeTo(null);
        removeFrame.setVisible(true);
    }
}

  
