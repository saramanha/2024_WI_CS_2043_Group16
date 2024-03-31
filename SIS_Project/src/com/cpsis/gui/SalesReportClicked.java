package com.cpsis.gui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalesReportClicked {
	public SalesReportClicked() {
		JFrame salesReportFrame = new JFrame();
		salesReportFrame.setTitle("View Sales History Reports");

        // Create the panel with spacing along the border
        JPanel filesPanel = new JPanel(new BorderLayout());
        filesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Get the files from a folder (replace "folderPath" with the actual path)
        File folder = new File("folderPath");
        File[] files = folder.listFiles();
        
        // Sort files by date
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed()); // Sort in descending order

        // Create a two-dimensional array to hold file names and dates
        String[][] data = new String[files.length][2];

        // Populate the data array with file names and dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        for (int i = 0; i < files.length; i++) {
            data[i][0] = files[i].getName(); // File name
            data[i][1] = dateFormat.format(new Date(files[i].lastModified())); // Date created
        }

        // Create column names
        String[] columnNames = {"File Name", "Date Created"};

        // Create a JTable with the file data
        JTable filesTable = new JTable(data, columnNames);
        filesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only single selection
        filesTable.setDefaultEditor(Object.class, null); // Make the table non-editable

        // Add a mouse listener to handle double-click events
        filesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Check for double-click
                    int selectedRow = filesTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String fileName = (String) filesTable.getValueAt(selectedRow, 0);
                        // Perform action with selected file (e.g., open the file)
                        System.out.println("Selected file: " + fileName);
                    }
                }
            }
        });

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(filesTable);
        filesPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the filesPanel to the frame
        salesReportFrame.setContentPane(filesPanel);
        salesReportFrame.pack();
        salesReportFrame.setLocationRelativeTo(null);
        salesReportFrame.setVisible(true);
    }
}
