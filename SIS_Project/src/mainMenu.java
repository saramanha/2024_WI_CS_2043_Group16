import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class mainMenu{
    public mainMenu() {
    	JFrame mainMenuFrame = new JFrame();
    	mainMenuFrame.setTitle("Supermarket Inventory System");
    	mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add padding around the content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainMenuFrame.setContentPane(contentPane);

        //Create TabbedDataTable with some test data
        Object[][][] data = {
            {
                {"Data 1", "Sample 1", "Extra 1"},
                {"Data 2", "Sample 2", "Extra 2"},
                {"Data 1", "Sample 1", "Extra 1"},
                {"Data 2", "Sample 2", "Extra 2"},
                {"Data 1", "Sample 1", "Extra 1"},
                {"Data 2", "Sample 2", "Extra 2"},
                {"Data 1", "Sample 1", "Extra 1"},
                {"Data 2", "Sample 2", "Extra 2"},
                {"Data 1", "Sample 1", "Extra 1"},
                {"Data 2", "Sample 6", "Extra 6"},
                {"Data 1", "Sample 7", "Extra 7"},
                {"Data 2", "Sample 2", "Extra 2"}
            },
            {
                {"Data 3", "Sample 3", "Extra 3"},
                {"Data 4", "Sample 4", "Extra 4"},
                {"Data 3", "Sample 3", "Extra 3"},
                {"Data 4", "Sample 4", "Extra 4"},
                {"Data 3", "Sample 3", "Extra 3"},
                {"Data 4", "Sample 4", "Extra 4"},
                {"Data 4", "Sample 4", "Extra 4"},
                {"Data 3", "Sample 3", "Extra 3"},
                {"Data 3", "Sample 3", "Extra 3"},
                {"Data 4", "Sample 4", "Extra 4"},
                {"Data 3", "Sample 3", "Extra 3"},
                {"Data 4", "Sample 4", "Extra 4"}
            }
        };

        Object[][] columnNames = {
            {"Column 1", "Column 2", "Column 3"},
            {"Column A", "Column B", "Column C"}
        };
        
        JPanel tabbedDataTablePanel = tabbedDataTable.createTabbedDataTable(data, columnNames);
        
        mainMenuFrame.add(tabbedDataTablePanel, BorderLayout.CENTER);

        //Create buttons, arrange in grid, and add padding
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // Add spacing between buttons
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Add spacing above the button panel
        JButton button1 = new JButton("Add Product to Stock Inventory");
        JButton button2 = new JButton("Move Product to Display Inventory");
        JButton button3 = new JButton("Remove Product from Display Inventory");
        JButton button4 = new JButton("View Sales History Reports");
        
        //Add button functionality when clicked
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new addToStockClicked();
            }
        });
        
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new moveToDisplayClicked();
            }
        });
        
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new removeFromDisplayClicked();
            }
        });
        
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new salesReportClicked();
            }
        });

        //Add buttons to the panel
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);

        //Add the button panel to the content pane
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        mainMenuFrame.pack();
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    public static void main(String[] args) {
    	JFrame.setDefaultLookAndFeelDecorated(true);
        new mainMenu();
    }
}