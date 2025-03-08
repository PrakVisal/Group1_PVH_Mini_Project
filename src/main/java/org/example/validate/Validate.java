package org.example.validate;

import org.example.config.color.Color;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Validate {
    public static double getValidUnitPrice(Scanner sc) {
        double unitPrice;
        while (true) {
            System.out.print("Enter Unit Price: ");
            if (sc.hasNextDouble()) {
                unitPrice = sc.nextDouble();
                if (unitPrice > 0) {
                    return unitPrice;
                } else {
                    System.out.println(Color.RED+"Unit price must be greater than zero."+ Color.RESET);
                }
            } else {
                System.out.println(Color.RED+"Invalid input. Please enter a valid unit price (e.g., 2.5)."+Color.RESET);
                sc.next();
            }
        }
    }

    public static int getValidQuantity(Scanner sc) {
        int quantity;
        while (true) {
            System.out.print("Enter Quantity: ");
            if (sc.hasNextInt()) {
                quantity = sc.nextInt();
                if (quantity > 0) {
                    return quantity;
                } else {
                    System.out.println(Color.RED+"Quantity must be greater than zero."+Color.RESET);
                }
            } else {
                System.out.println(Color.RED+"Invalid input. Please enter a valid quantity (integer)."+Color.RESET);
                sc.next();
            }
        }
    }

    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println(Color.RED+"Name cannot be empty."+Color.RESET);
            return false;
        }
        return true;
    }
    public static boolean validateProductId(int id) {
        try {
            if (id <= 0) {
                System.out.println(Color.RED + "Product ID must be greater than zero." + Color.RESET);
                return false;
            }
            return true;
        } catch (InputMismatchException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean validateProduct(int id, String name, int quantity, double unitPrice, LocalDate importDate) {
        if (!validateName(name)) {
            return false;
        }
        if (quantity <= 0) {
            System.out.println(Color.RED+"Quantity must be greater than zero."+Color.RESET);
            return false;
        }
        if (unitPrice <= 0) {
            System.out.println(Color.RED+"Unit price must be greater than zero."+Color.RESET);
            return false;
        }
        if (importDate == null) {
            System.out.println(Color.RED+"Import date cannot be null."+Color.RESET);
            return false;
        }
        return true;
    }
}
