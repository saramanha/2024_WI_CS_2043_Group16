package com.cpsis.filehandling;
import com.cpsis.database.DatabaseManager;
import com.opencsv.CSVWriter; // Import for CSVWriter
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static String readFolderPathFromConfigFile() {
        // Read the folder path from the configuration file
        try {
            String configFilePath = "config/config.txt";
            List<String> lines = Files.readAllLines(Paths.get(configFilePath));
            if (!lines.isEmpty()) {
                return lines.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createNewSalesReport() {
        List<String[]> salesHistoryData = new ArrayList<>();
        try {
            // Assuming DatabaseManager.getSalesHistory() now returns a List<String[]> for simplicity
            salesHistoryData = DatabaseManager.getSalesHistory();
        } catch (SQLException e) {
            System.out.println("Error getting sales history data");
            e.printStackTrace();
            return;
        }
        //Get current date
        LocalDate currentDate = LocalDate.now();
        //Format date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = currentDate.format(formatter);
        //Set new files name and getting the path, change to .csv extension
        String fileName = dateString + "-SalesReport.csv";
        Path newSalesReportPath = Paths.get(readFolderPathFromConfigFile(), fileName);

        try (CSVWriter writer = new CSVWriter(new FileWriter(newSalesReportPath.toString()))) {
            // Write headers
            String[] header = {"Sale ID", "Date Sold", "Product ID", "Product Name", "Quantity Sold", "Product Price", "Sale Discount", "Total Revenue"};
            writer.writeNext(header);
            // Write sales history data
            for (String[] record : salesHistoryData) {
                writer.writeNext(record);
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
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
