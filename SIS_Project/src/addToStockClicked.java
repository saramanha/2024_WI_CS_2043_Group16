import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class addToStockClicked {
    public addToStockClicked() {
    	JFrame addProductFrame = new JFrame();
    	addProductFrame.setTitle("Add Product to Stock Inventory");
    	addProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		addProductFrame.setContentPane(contentPanel);

        // Create the panel with buttons for options
        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0)); // Center alignment, horizontal gap of 25 pixels

        // Option 1 button
        JButton option1Button = new JButton("New Product");
        option1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewOption();
                addProductFrame.dispose();
            }
        });

        // Option 2 button
        JButton option2Button = new JButton("Existing Product");
        option2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExistingOption();
                addProductFrame.dispose();
            }
        });

        // Add buttons to the optionPanel
        optionPanel.add(option1Button);
        optionPanel.add(option2Button);

        // Add the optionPanel to the content pane
        contentPanel.add(optionPanel, BorderLayout.CENTER);

        addProductFrame.pack();
        addProductFrame.setLocationRelativeTo(null);
        addProductFrame.setVisible(true);
    }

    // Method to display panel with 4 fields on one row and another row below with 3 fields and a submit button
    private void addNewOption() {
    	// Create a new JFrame for Option 1
        JFrame option1Frame = new JFrame("Add New Product to Stock Inventory");
        option1Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create the panel with spacing along the border
        JPanel option1Panel = new JPanel(new GridLayout(3, 1, 0, 10)); // 3 rows, 1 column, vertical gap of 10 pixels
        option1Panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add spacing along the border

        // First row with 4 fields
        JPanel row1 = new JPanel(new FlowLayout());
        row1.add(new JLabel("Name:"));
        row1.add(new JTextField(10));
        row1.add(new JLabel("Category:"));
        row1.add(new JTextField(10));
        row1.add(new JLabel("Manufacturer:"));
        row1.add(new JTextField(10));
        row1.add(new JLabel("Price:"));
        row1.add(new JTextField(10));

        // Second row with 3 fields and a submit button
        JPanel row2 = new JPanel(new FlowLayout());
        row2.add(new JLabel("Expiration Date:"));
        row2.add(new JTextField(10));
        row2.add(new JLabel("Location(Aisle#-Shelf#):"));
        row2.add(new JTextField(10));
        row2.add(new JLabel("Quantity:"));
        row2.add(new JTextField(10));
        row2.add(new JLabel("Discount(%):"));
        row2.add(new JTextField(10));
        
        JPanel row3 = new JPanel(new FlowLayout());
        JButton submitButton = new JButton("Submit");
        row3.add(submitButton);

        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Handle button click
            }
        });

        // Add rows to the option1Panel
        option1Panel.add(row1);
        option1Panel.add(row2);
        option1Panel.add(row3);

        // Add the option1Panel to the JFrame
        option1Frame.add(option1Panel);
        option1Frame.pack();
        option1Frame.setLocationRelativeTo(null);
        option1Frame.setVisible(true);
    }

    // Method to display panel that looks exactly like Functionality 1
    private void addExistingOption() {
    	// Create a new JFrame for Option 2
        JFrame option2Frame = new JFrame("Add Existing Product to Stock Inventory");
        option2Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create the panel with spacing along the border
        JPanel option2Panel = new JPanel(new BorderLayout());
		option2Panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        option2Frame.add(option2Panel);
        
        // Search Panel        
        JPanel searchPanel = new JPanel(new FlowLayout());
		JTextField searchField = new JTextField(20);
		JButton searchButton = new JButton("Search");
		searchPanel.add(new JLabel("Search by Name or ID:"));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		
		// Mock data for demonstration
		String[][] data = {
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"},
			{"Item 1", "Description 1", "Item 1", "Description 1", "Test"}
		};
		String[] columnNames = {"Product ID", "Product Name", "Price", "Category Name", "Manufaturer Name"};
		JTable resultTable = new JTable(data, columnNames);
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
		JTextField field1 = new JTextField(10);
		JTextField field2 = new JTextField(10);
		JTextField field3 = new JTextField(10);
		JTextField field4 = new JTextField(10);

		JButton submitButton = new JButton("Submit");
		additionalFieldsPanel.add(new JLabel("Expiration Date:"));
		additionalFieldsPanel.add(field1);
		additionalFieldsPanel.add(new JLabel("Location(Aisle#-Shelf#):"));
		additionalFieldsPanel.add(field2);
		additionalFieldsPanel.add(new JLabel("Quantity:"));
		additionalFieldsPanel.add(field3);
		additionalFieldsPanel.add(new JLabel("Discount(%):"));
		additionalFieldsPanel.add(field4);
		additionalFieldsPanel.add(Box.createHorizontalStrut(10)); // Add horizontal spacing of 10 pixels
		additionalFieldsPanel.add(submitButton);
		
		//Example of using the slected row of the datatable
		submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) { // If a row is selected
                    String selectedItem = (String) resultTable.getValueAt(selectedRow, 0); // Get the value of the first column
                    // Use selectedItem as needed
                    System.out.println("Selected item: " + selectedItem);
                } else {
                	JFrame errorFrame = new JFrame();
                    JOptionPane.showMessageDialog(errorFrame, "Please select a row.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
		
		// Add components to the content pane
		option2Panel.add(searchPanel, BorderLayout.NORTH);
		option2Panel.add(tablePanel, BorderLayout.CENTER);
		option2Panel.add(additionalFieldsPanel, BorderLayout.SOUTH);
		
		option2Frame.add(option2Panel);
	    option2Frame.pack();
	    option2Frame.setLocationRelativeTo(null);
	    option2Frame.setVisible(true);
	}
}
