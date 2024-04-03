package com.cpsis.filehandling;

import java.io.*;
import java.nio.file.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SalesReportSetup {
	private static final String CONFIG_FILE_PATH = "config/config.txt";

    public static void checkFirstRun() {
        // Check if the configuration file exists
        boolean isFirstRun = !Files.exists(Paths.get(CONFIG_FILE_PATH));

        if (isFirstRun) {
            // Perform setup
            performSetup();
        } else {
            // Skip setup
            System.out.println("Setup already completed. Skipping.");
        }
    }
    
    private static void performSetup() {
        boolean folderSelected = false;
        File selectedFolder = null;

        while (!folderSelected) {
            // Create a file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose or Create a Folder for Sales Reports");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            // Show the file chooser dialog
            int result = fileChooser.showDialog(null, "Select Folder");

            // Process the user's selection
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFolder = fileChooser.getSelectedFile();
                folderSelected = true;
            } else {
                // User did not select a folder, show an error message
                JOptionPane.showMessageDialog(null, "You must select a folder for sales reports.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // At this point, selectedFolder contains the selected directory
        String folderPath = selectedFolder.getAbsolutePath();

        // Store the folder path for future use (e.g., in the configuration file)
        System.out.println("Selected folder: " + folderPath);

        // Write the folder path to the configuration file
        writeFolderPathToConfigFile(folderPath);

        // Provide feedback to the user
        JOptionPane.showMessageDialog(null, "Sales reports will be saved to:\n" + folderPath);
    }

    private static void writeFolderPathToConfigFile(String folderPath) {
        // Write the folder path to the configuration file
        try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
            writer.write(folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
