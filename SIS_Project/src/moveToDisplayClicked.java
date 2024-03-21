import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class moveToDisplayClicked {
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
		
		// Mock data for demonstration
		String[][] data = {
			{"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"},
	        {"Data 1", "Sample 1", "Extra 1", "Data 1", "Sample 1", "Extra 1", "Test"}
		};
		String[] columnNames = {"Stock ID", "Product Name", "Quantity", "Price", "Location", "Expiration Date", "Discount"};
		JTable resultTable = new JTable(data, columnNames);
		resultTable.setDefaultEditor(Object.class, null); // Make the table non-editable
		
		JScrollPane scrollPane = new JScrollPane(resultTable);
		JPanel tablePanel = new JPanel(new BorderLayout()); // Wrap the scroll pane in a panel
		tablePanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add padding around the table
		tablePanel.setSize(500,500);
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
		JButton submitButton = new JButton("Submit");
		additionalFieldsPanel.add(new JLabel("Quantity:"));
		additionalFieldsPanel.add(field1);
		additionalFieldsPanel.add(Box.createHorizontalStrut(10)); // Add horizontal spacing of 10 pixels
		additionalFieldsPanel.add(new JLabel("Location(Aisle#-Shelf#):"));
		additionalFieldsPanel.add(field2);
		additionalFieldsPanel.add(Box.createHorizontalStrut(10)); // Add horizontal spacing of 10 pixels
		additionalFieldsPanel.add(new JLabel("Discount(%):"));
		additionalFieldsPanel.add(field3);
		additionalFieldsPanel.add(Box.createHorizontalStrut(10)); // Add horizontal spacing of 10 pixels
		additionalFieldsPanel.add(submitButton);
		
		//Example of using the slected row of the datatable
		submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) { // If a row is selected
                    String selectedItem = (String) resultTable.getValueAt(selectedRow, 0); // Get the value of the first column
                    // Use the selectedItem as needed
                    System.out.println("Selected item: " + selectedItem);
                } else {
                	JFrame errorFrame = new JFrame();
                    JOptionPane.showMessageDialog(errorFrame, "Please select a row.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
		
		// Add components to the content pane
		    contentPane.add(searchPanel, BorderLayout.NORTH);
		    contentPane.add(tablePanel, BorderLayout.CENTER);
		    contentPane.add(additionalFieldsPanel, BorderLayout.SOUTH);
		
		    moveProductFrame.setSize(810,348);
		    moveProductFrame.setLocationRelativeTo(null);
		    moveProductFrame.setVisible(true);
		}
}