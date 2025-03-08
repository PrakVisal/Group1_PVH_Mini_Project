package org.example.controller;


import org.example.model.Product;
import org.example.model.dao.ProductDAOImpl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ProductController {
    ProductDAOImpl productDAO = new ProductDAOImpl();
    public void saveProduct(List<Product> product) throws SQLException {
        productDAO.saveProduct(product);
    }
    public void writeProduct(List<Product> product) throws SQLException {
            productDAO.writeProduct(product);
    }
    public void unSaveProduct(List<Product> product) throws SQLException {
        productDAO.unSaveProduct(product);
    }
    public void seachProductbyID() throws SQLException {
        productDAO.searchProductbyID();
    }
    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }
    public void deleteProductById() throws SQLException {
        productDAO.deleteProduct();
    }
    public void searchProductByName(){
        productDAO.searchProductbyName();
    }

    public void backupProducts() {
        String backupFilePath = "backups/backup" + System.currentTimeMillis() + ".txt"; // Example of dynamic naming
        try {
            productDAO.backupProducts(backupFilePath);
        } catch (SQLException e) {
            System.out.println("Backup failed: " + e.getMessage());
        }
    }

    public void restoreProducts() {
        String[] backups = productDAO.listBackupFiles();
        if (backups.length == 0) {
            System.out.println("No backup files available to restore.");
            return;
        }

        System.out.println("Available Backup Files:");
        Arrays.stream(backups).forEach(System.out::println);

        System.out.print("Enter the name of the backup file to restore (e.g., backup1.txt): ");
        Scanner scanner = new Scanner(System.in);
        String backupFileName = scanner.nextLine();
        String backupFilePath = "backups/" + backupFileName;

        try {
            productDAO.restoreProducts(backupFilePath);
        } catch (SQLException e) {
            System.out.println("Restore failed: " + e.getMessage());
        }
    }


}

