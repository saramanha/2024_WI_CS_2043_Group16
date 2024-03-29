import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addToStockClicked {
    private static final String LOCATION_PATTERN = "\\d+-\\d+";
    private JTable resultTable;
    private Object[][] data;
    private Map<String, Integer> categoryMap;
    private Map<String, Integer> manufacturerMap;
    private String prodName;
    private int catID;
    private int mfrID;
    private BigDecimal prodPrice;
    private int prodID;
    private int invQty;
    private String invLocation;
    private LocalDate expDate;
    private BigDecimal discount;
	
	
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
        
        //Name Field
        row1.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(15);
        row1.add(nameField);
        
        //Category Field
        row1.add(new JLabel("Category:"));
        JComboBox<String> categoryComboBox = new JComboBox<>();
        try {
            categoryMap = DatabaseManager.getCategories();
            for (String categoryName : categoryMap.keySet()) {
                categoryComboBox.addItem(categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
        categoryComboBox.setSelectedIndex(-1);
        categoryComboBox.setEditable(true); // Allow user to type in new entry
        row1.add(categoryComboBox);

        //Manufacturer Field
        row1.add(new JLabel("Manufacturer:"));
        JComboBox<String> manufacturerComboBox = new JComboBox<>();
        try {
            manufacturerMap = DatabaseManager.getManufacturers();
            for (String manufacturerName : manufacturerMap.keySet()) {
            	manufacturerComboBox.addItem(manufacturerName);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
        manufacturerComboBox.setSelectedIndex(-1);
        manufacturerComboBox.setEditable(true); // Allow user to type in new entry
        row1.add(manufacturerComboBox);
        
        //Price Field
        row1.add(new JLabel("Price ($):"));
        JTextField priceField = new JTextField(10);
        row1.add(priceField);

        // Second row with 3 fields and a submit button
        JPanel row2 = new JPanel(new FlowLayout());
        //Expiration Date datepicker
        row2.add(new JLabel("Expiration Date:"));
        JDateChooser expirationDateChooser = new JDateChooser();
        expirationDateChooser.setPreferredSize(new Dimension(125, 20));
        row2.add(expirationDateChooser);
        
        //Location Field
        row2.add(new JLabel("Location(Aisle#-Shelf#):"));
        JTextField locationField = new JTextField(10);
        row2.add(locationField);
        
        //Quantity Field
        row2.add(new JLabel("Quantity:"));
        JTextField qtyField = new JTextField(10);
        row2.add(qtyField);
        
        //Discount Field
        row2.add(new JLabel("Discount(%):"));
        JTextField discountField = new JTextField(10);
        row2.add(discountField);
        
        //Submit button
        JPanel row3 = new JPanel(new FlowLayout());
        JButton submitButton = new JButton("Submit");
        row3.add(submitButton);

        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	prodName = nameField.getText().trim();
            	//Checking and processing the category field
            	//Either user did input correctly and catID was given the CategoryID for the record or did not input correctly
            	String selectedCategory = (String) categoryComboBox.getSelectedItem();
            	if (!checkAndProcessCategoryBox(selectedCategory)) {
                    JOptionPane.showMessageDialog(option1Frame, "Please select or enter a valid category.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            	
            	//Checking and processing the manufacturer field
            	//Either user did input correctly and mfrID was given the ManufacturerID for the record or did not input correctly
            	String selectedManufacturer = (String) manufacturerComboBox.getSelectedItem();
            	if (!checkAndProcessManufacturerBox(selectedManufacturer)) {
                    JOptionPane.showMessageDialog(option1Frame, "Please select or enter a valid manufacturer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            	
            	//Getting the price input and formatting it
            	try {
	            	String priceText = priceField.getText();
	            	priceText = priceText.replaceAll(",", "");
	            	prodPrice = new BigDecimal(priceText).setScale(2, RoundingMode.HALF_UP);
            	} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(option1Frame, "Invalid price format. Please enter a valid numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
            	
            	//Getting the expiration date and changing it to LocalDate datatype
            	expDate = getDateFromJDateChooser(expirationDateChooser);
            	
            	//Getting the location input and making sure it is correct format
            	invLocation = locationField.getText();
            	if(!isValidLocation(invLocation)) {
            		JOptionPane.showMessageDialog(option1Frame, "Invalid location format. Please format as: Aisle#-Shelf#.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
            	
            	//Getting the quantity input and making sure it is formatted correctly
            	try {
            	    invQty = Integer.parseInt(qtyField.getText());
            	} catch (NumberFormatException e1) {
            		JOptionPane.showMessageDialog(option1Frame, "Invalid quantity. Please use numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
            	
            	//Getting the inputted discount percentage, converting it to decimal format, and making sure input is valid
            	discount = convertDiscountToBigDecimal(discountField.getText());
            	if(discount == null) {
            		JOptionPane.showMessageDialog(option1Frame, "Invalid discount format. Please enter a whole number for the discount percentage", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
            	
            	//Creating product object and using it to add product to database
            	Product newProd = new Product(prodName, catID, mfrID, prodPrice);
            	try {
					prodID = DatabaseManager.addNewProduct(newProd);
				} catch (SQLException e1) {
					System.out.println("Failed to add new product");
					e1.printStackTrace();
				}
            	
            	//Creating an InventoryRecord object and using that to add record to database
            	InventoryRecord newRecord = new InventoryRecord(prodID, invQty, invLocation, expDate, discount);
            	try {
					DatabaseManager.addProductToStockInv(newRecord);
				} catch (SQLException e1) {
					System.out.println("Failed to add new product to stock inventory");
					e1.printStackTrace();
				}
            	//Close window once everything is processed and database is updated
            	option1Frame.dispose();
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
		JButton revertButton = new JButton("Remove Filter");
		revertButton.setVisible(false);
		searchPanel.add(new JLabel("Search by Name or ID:"));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.add(revertButton);
		
		//Setting up JTable with columns and the relevent data
		String[] columnNames = {"Product ID", "Product Name", "Price", "Category Name", "Manufaturer Name"};
		try {
			data = DatabaseManager.getProductList();
		} catch (SQLException e) {
			System.out.println("Error getting Product list data");
			e.printStackTrace();
		}
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
		//Expiration Date datepicker
		additionalFieldsPanel.add(new JLabel("Expiration Date:"));
        JDateChooser expirationDateChooser = new JDateChooser();
        expirationDateChooser.setPreferredSize(new Dimension(125, 20));
        additionalFieldsPanel.add(expirationDateChooser);
		additionalFieldsPanel.add(Box.createHorizontalStrut(5)); // Add horizontal spacing of 10 pixels
        
		//Location Field
        additionalFieldsPanel.add(new JLabel("Location(Aisle#-Shelf#):"));
        JTextField locationField = new JTextField(10);
        additionalFieldsPanel.add(locationField);
		additionalFieldsPanel.add(Box.createHorizontalStrut(5)); // Add horizontal spacing of 10 pixels

        //Quantity Field
        additionalFieldsPanel.add(new JLabel("Quantity:"));
        JTextField qtyField = new JTextField(10);
        additionalFieldsPanel.add(qtyField);
		additionalFieldsPanel.add(Box.createHorizontalStrut(5)); // Add horizontal spacing of 10 pixels

        //Discount Field
        additionalFieldsPanel.add(new JLabel("Discount(%):"));
        JTextField discountField = new JTextField(10);
        additionalFieldsPanel.add(discountField);
		additionalFieldsPanel.add(Box.createHorizontalStrut(5)); // Add horizontal spacing of 10 pixels

        JButton submitButton = new JButton("Submit");
        additionalFieldsPanel.add(submitButton);
		
		//Example of using the slected row of the datatable
		submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) { // If a row is selected
                	//Get the value of the first column of the selected row and use that as the prodID
                    prodID = (Integer) resultTable.getValueAt(selectedRow, 0);
                } else {
                    JOptionPane.showMessageDialog(option2Frame, "Please select a row.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
                }
                
            	//Getting the expiration date and changing it to LocalDate datatype
            	expDate = getDateFromJDateChooser(expirationDateChooser);
            	
            	//Getting the location input and making sure it is correct format
            	invLocation = locationField.getText();
            	if(!isValidLocation(invLocation)) {
            		JOptionPane.showMessageDialog(option2Frame, "Invalid location format. Please format as: Aisle#-Shelf#.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
            	
            	//Getting the quantity input and making sure it is formatted correctly
            	try {
            	    invQty = Integer.parseInt(qtyField.getText());
            	} catch (NumberFormatException e1) {
            		JOptionPane.showMessageDialog(option2Frame, "Invalid quantity. Please use numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
            	
            	//Getting the inputted discount percentage, converting it to decimal format, and making sure input is valid
            	discount = convertDiscountToBigDecimal(discountField.getText());
            	if(discount == null) {
            		JOptionPane.showMessageDialog(option2Frame, "Invalid discount format. Please enter a whole number for the discount percentage", "Error", JOptionPane.ERROR_MESSAGE);
					return;
            	}
            	
            	//Creating an InventoryRecord object and using that to add record to database
            	InventoryRecord newRecord = new InventoryRecord(prodID, invQty, invLocation, expDate, discount);
            	try {
					DatabaseManager.addProductToStockInv(newRecord);
				} catch (SQLException e1) {
					System.out.println("Failed to add new product to stock inventory");
					e1.printStackTrace();
				}
            	//Close window once everything is processed and database is updated
            	option2Frame.dispose();
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
	            else if (TableFilterUtils.isNumeric(searchCriteria)) {
	                // Filter by product ID (1st column)
	                filteredData = TableFilterUtils.filterDataByID(data, Integer.parseInt(searchCriteria));
	                revertButton.setVisible(true);
	            } else {
	                // Filter by product name (2nd column)
	            	filteredData = TableFilterUtils.filterDataByName(data, searchCriteria);
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
		option2Panel.add(searchPanel, BorderLayout.NORTH);
		option2Panel.add(tablePanel, BorderLayout.CENTER);
		option2Panel.add(additionalFieldsPanel, BorderLayout.SOUTH);
		
		option2Frame.add(option2Panel);
	    option2Frame.pack();
	    option2Frame.setLocationRelativeTo(null);
	    option2Frame.setVisible(true);
	}
    
    private boolean checkAndProcessCategoryBox(String selectedCategory) {
        // If both selectedCategory and typedCategory are empty or null, the user left the combo box empty
        if (selectedCategory == null || selectedCategory.trim().isEmpty()) {
            return false;
        }
        
        boolean isNewCategory = !categoryMap.containsKey(selectedCategory);

        // If selectedCategory is empty or null, it means the user has typed a new category
        if (isNewCategory) {
            Category newCat = new Category(selectedCategory);
            try {
				catID = DatabaseManager.addNewCategory(newCat);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed to add new category");
			}
        }
        // User selected one of the options in the dropdown
        else {
        	catID = categoryMap.get(selectedCategory);
        }
        
        return true;
    }
    
    private boolean checkAndProcessManufacturerBox(String selectedManufacturer) {
        // If both selectedCategory and typedCategory are empty or null, the user left the combo box empty
        if (selectedManufacturer == null || selectedManufacturer.isEmpty()) {
            return false;
        }
        
        boolean isNewManufacturer = !manufacturerMap.containsKey(selectedManufacturer);
        
        // If selectedCategory is empty or null, it means the user has typed a new category
        if (isNewManufacturer) {
            Manufacturer newMfr = new Manufacturer(selectedManufacturer);
            try {
				mfrID = DatabaseManager.addNewManufacturer(newMfr);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed to add new manufacturer");
			}
        }
        // User selected one of the options in the dropdown
        else {
        	mfrID = manufacturerMap.get(selectedManufacturer);
        }
        
        return true;
    }
    
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
    
    public static LocalDate getDateFromJDateChooser(JDateChooser dateChooser) {
        java.util.Date selectedDate = dateChooser.getDate();
        if (selectedDate != null) {
            return selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            return null; // No date selected, return null
        }
    }
}
