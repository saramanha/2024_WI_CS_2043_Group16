import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class removeFromDisplayClicked{
	public removeFromDisplayClicked() {
		JFrame removeFrame = new JFrame();
        removeFrame.setTitle("Remove Product from Display Inventory");
        removeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Get the content pane and add empty border to it for padding
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        removeFrame.setContentPane(contentPane);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search by Name or ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Mock data for demonstration
        String[][] data = {
                {"Item 1", "Description 1"},
                {"Item 2", "Description 2"},
                {"Item 3", "Description 3"},
                {"Item 4", "Description 4"},
                {"Item 5", "Description 5"},
                {"Item 6", "Description 6"},
                {"Item 7", "Description 7"},
                {"Item 8", "Description 8"},
                {"Item 9", "Description 9"},
                {"Item 10", "Description 10"},
                {"Item 11", "Description 11"}
        };
        String[] columnNames = {"Item", "Description"};
        JTable resultTable = new JTable(data, columnNames);
        resultTable.setDefaultEditor(Object.class, null); // Make the table non-editable
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        JPanel tablePanel = new JPanel(new BorderLayout()); // Wrap the scroll pane in a panel
        tablePanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add padding around the table

        // Set the maximum number of visible rows for the table
        resultTable.setPreferredScrollableViewportSize(new Dimension(resultTable.getPreferredScrollableViewportSize().width, resultTable.getRowHeight() * 10));

        tablePanel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the panel

        // Additional Fields Panel
        JPanel additionalFieldsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Align components to the right
        JTextField field1 = new JTextField(10);
        JButton submitButton = new JButton("Submit");
        additionalFieldsPanel.add(new JLabel("Field 1:"));
        additionalFieldsPanel.add(field1);
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

        removeFrame.pack();
        removeFrame.setLocationRelativeTo(null);
        removeFrame.setVisible(true);
    }
}
