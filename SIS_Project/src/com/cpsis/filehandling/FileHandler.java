package com.cpsis.filehandling;
import com.cpsis.database.DatabaseManager;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FileHandler {
	public static String readFolderPathFromConfigFile() {
        // Read the folder path from the configuration file
        try {
            // Read all lines from the configuration file
            String configFilePath = "config/config.txt";
            List<String> lines = Files.readAllLines(Paths.get(configFilePath));

            // Check if the file is not empty
            if (!lines.isEmpty()) {
                // Return the folder path (first line)
                return lines.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static void createNewSalesReport() {
		List<String> salesHistoryData = null;
		try {
			salesHistoryData = DatabaseManager.getSalesHistory();
		} catch (SQLException e) {
			System.out.println("Error getting sales history data");
			e.printStackTrace();
		}
		//Get current date
		LocalDate currentDate = LocalDate.now();
		//Format date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateString = currentDate.format(formatter);
		//Set new files name and getting the path
		String fileName = dateString + "-SalesReport";
		Path newSalesReportPath = Paths.get(readFolderPathFromConfigFile(), fileName);
		 
		try (PrintWriter writer = new PrintWriter(new FileWriter(newSalesReportPath.toString()))) {
			// Write headers
			writer.println(String.format(" %-8s | %-10s | %-10s | %-20s | %-13s | %-13s | %-15s | %-15s",
	                "Sale ID", "Date Sold", "Product ID", "Product Name", "Quantity Sold", "Product Price", "Sale Discount", "Total Revenue"));			
			// Write sales history data
			for (String salesRecord : salesHistoryData) {
			    writer.println(salesRecord);
			}
			
			System.out.println("Sales report written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write sales report.");
        }
	}
	
	public static boolean isWeekSinceLastFileCreation() {
        Path salesReportFolderPath = Paths.get(readFolderPathFromConfigFile());
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(salesReportFolderPath)) {
            Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
            for (Path file : stream) {
                BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                Instant creationTime = attrs.creationTime().toInstant();
                if (creationTime.isAfter(oneWeekAgo)) {
                    // If any file was created within the last week, return false
                    return false;
                }
            }
            // If all files were created at least a week ago, return true
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Handle the exception appropriately
        }
    }
}