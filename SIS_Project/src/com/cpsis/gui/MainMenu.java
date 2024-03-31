package com.cpsis.gui;
import com.cpsis.database.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class MainMenu{
    public MainMenu() {
    	JFrame mainMenuFrame = new JFrame();
    	mainMenuFrame.setTitle("Supermarket Inventory System");
    	mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	// Add padding around the content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainMenuFrame.setContentPane(contentPane);
        
        JPanel tabbedDataTablePanel = TabbedDataTable.createTabbedDataTable();
        
        mainMenuFrame.add(tabbedDataTablePanel, BorderLayout.CENTER);

        //Create buttons, arrange in grid, and add padding
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 10)); // Add spacing between buttons
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Add spacing above the button panel
        JButton button1 = new JButton("Add Product to Stock Inventory");
        JButton button2 = new JButton("Move Product to Display Inventory");
        JButton button3 = new JButton("Remove Product from Display Inventory");
        JButton button4 = new JButton("View Sales History Reports");
        
        //Add button functionality when clicked
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddToStockClicked();
            }
        });
        
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MoveToDisplayClicked();
            }
        });
        
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RemoveFromDisplayClicked();
            }
        });
        
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SalesReportClicked();
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
        mainMenuFrame.setMinimumSize(new Dimension(850, 0));
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    public static void main(String[] args) {
    	JFrame.setDefaultLookAndFeelDecorated(true);
    	
    	// Establish the database connection
        try {
            DatabaseManager.checkConnection();
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
        	JOptionPane.showMessageDialog(null, "Failed to connect to the database:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            // Exit the program
            System.exit(1);
        }
        
        // Add a shutdown hook to close the database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DatabaseManager.closeConnection();
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Failed to close the database connection.");
                e.printStackTrace();
            }
        }));
    	
        new MainMenu();
    }
}