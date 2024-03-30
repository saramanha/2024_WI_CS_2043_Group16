import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.x.protobuf.MysqlxCrud.Update;

import java.util.ArrayList;
import java.util.List;

public class moveToDisplayClicked {
    private Object[][] data;
    private JTable resultTable;
    private int stockQty;
    private int inputQty;
    private int stockID;
    private int prodID;
    private String stockLocation;
    private Date expiration;
    private BigDecimal stockDiscount;
    private String displayLocation;
    private BigDecimal displayDiscount;

	public moveToDisplayClicked() {
		JFrame moveProductFrame = new JFrame();
		moveProductFrame.setTitle("Move Product to Display Inventory");
		moveProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Get the content pane and add empty border to it for padding
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        moveProductFrame.add(contentPane);
		
		// Search Panel
		JPanel searchPanel = new JPanel(new FlowLayout());
		JTextField searchField = new JTextField(20);
		JButton searchButton = new JButton("Search");
		searchPanel.add(new JLabel("Search by Name or ID:"));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		
		try {
			data = DatabaseManager.getStockInventory();
		} catch (SQLException e) {
			System.out.println("Error getting display inventory data");
			e.printStackTrace();
		}

		String[] columnNames = {"Stock ID", "Product ID", "Product Name", "Quantity", "Price", "Location", "Expiration Date", "Discount"};
		resultTable = new JTable(data, columnNames);
		resultTable.setDefaultEditor(Object.class, null); // Make the table non-editable
		
		JScrollPane scrollPane = new JScrollPane(resultTable);
		JPanel tablePanel = new JPanel(new BorderLayout()); // Wrap the scroll pane in a panel
		tablePanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add padding around the table
		tablePanel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the panel
		
		/// Set the preferred viewport size based on the number of rows
        int numRows = Math.min(resultTable.getRowCount(), 10);
        resultTable.setPreferredScrollableViewportSize(new Dimension(resultTable.getPreferredScrollableViewportSize().width, resultTable.getRowHeight() * numRows));

        tablePanel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the panel
        
		// Additional Fields Panel
		JPanel additionalFieldsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING)); // Align components to the right
		JTextField quantityField = new JTextField(10);
		JTextField locationField = new JTextField(10);
		JTextField discountField = new JTextField(10);
		JButton submitButton = new JButton("Submit");
		additionalFieldsPanel.add(new JLabel("Quantity:"));
		additionalFieldsPanel.add(quantityField);
		additionalFieldsPanel.add(Box.createHorizontalStrut(10)); // Add horizontal spacing of 10 pixels
		additionalFieldsPanel.add(new JLabel("Location(Aisle#-Shelf#):"));
		additionalFieldsPanel.add(locationField);
		additionalFieldsPanel.add(Box.createHorizontalStrut(10)); // Add horizontal spacing of 10 pixels
		additionalFieldsPanel.add(new JLabel("Discount(%):"));
		additionalFieldsPanel.add(discountField);
		additionalFieldsPanel.add(Box.createHorizontalStrut(10)); // Add horizontal spacing of 10 pixels
		additionalFieldsPanel.add(submitButton);
		
		//Example of using the slected row of the datatable
		submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//Getting the selected row as input
            	int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) { // If a row is selected
                	//Get the value of the first column of the selected row and use that as the prodID
                	stockID = (Integer) resultTable.getValueAt(selectedRow, 0);
                	prodID = (Integer) resultTable.getValueAt(selectedRow, 1);
                	stockQty = (Integer) resultTable.getValueAt(selectedRow, 3);
                	expiration = (Date) resultTable.getValueAt(selectedRow, 6);
                	stockDiscount = (BigDecimal) resultTable.getValueAt(selectedRow, 7);
                } else {
                    JOptionPane.showMessageDialog(moveProductFrame, "Please select a row.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
                }
                
                //Getting the quantity input
                try {
            	    inputQty = Integer.parseInt(quantityField.getText());
            	} catch (NumberFormatException e1) {
            		JOptionPane.showMessageDialog(moveProductFrame, "Invalid quantity. Please use numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
                if(inputQty > stockQty) {
                	JOptionPane.showMessageDialog(moveProductFrame, "Invalid quantity. Please ensure there is enough in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                	return;
                }
                
                //Getting the location inputted
            	displayLocation = locationField.getText();
            	if(!GUIUtils.isValidLocation(displayLocation)) {
            		JOptionPane.showMessageDialog(moveProductFrame, "Invalid location format. Please format as: Aisle#-Shelf#.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
                
                //Getting the discount inputted
            	displayDiscount = GUIUtils.convertDiscountToBigDecimal(discountField.getText());
            	if(displayDiscount == null) {
            		JOptionPane.showMessageDialog(moveProductFrame, "Invalid discount format. Please enter a whole number for the discount percentage", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
            	
            	if(displayDiscount != stockDiscount) {
            		try {
						DatabaseManager.updateDiscount(displayDiscount, stockID);
					} catch (SQLException e1) {
						System.out.println("Failed to update discount in stock inventory");
						e1.printStackTrace();
						return;
					}
            	}
            	
            	try {
					DatabaseManager.moveProductToDisplayInv(stockID, prodID, inputQty, displayLocation, expiration, displayDiscount);
				} catch (SQLException e1) {
					System.out.println("Failed to move record to display inventory");
					e1.printStackTrace();
					return;
				}
                
            	moveProductFrame.dispose();
            }
        });
		
		// Add components to the content pane
		    contentPane.add(searchPanel, BorderLayout.NORTH);
		    contentPane.add(tablePanel, BorderLayout.CENTER);
		    contentPane.add(additionalFieldsPanel, BorderLayout.SOUTH);
		
		    moveProductFrame.pack();
		    moveProductFrame.setLocationRelativeTo(null);
		    moveProductFrame.setVisible(true);
	}
}
