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

         try {
        			data = DatabaseManager.getProductList();
        		} catch (SQLException e1) {
        			System.out.println("Error getting Product list data");
        			e1.printStackTrace();
        		}

        
        JPanel tabbedDataTablePanel = tabbedDataTable.createTabbedDataTable(data);
        
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
        
        mainMenuFrame.setSize(725, 356);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    public static void main(String[] args) {
    	JFrame.setDefaultLookAndFeelDecorated(true);
        new mainMenu();
    }
}