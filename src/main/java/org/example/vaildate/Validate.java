package org.example.vaildate;

import java.time.LocalDate;

public class Validate {
        public static boolean validateProduct(int id, String name, int quantity, double unitPrice, LocalDate importDate) {
            if (name == null || name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return false;
            }
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return false;
            }
            if (unitPrice <= 0) {
                System.out.println("Unit price must be greater than zero.");
                return false;
            }
            if (importDate == null) {
                System.out.println("Import date cannot be null.");
                return false;
            }

            return true;
        }

//    public static boolean validateID (String name){
//        if (name == null || name.isEmpty()) {
//            System.out.println("Name cannot be empty.");
//            return false;
//        }
//        return true;
//    }
//
//    public static boolean validateUnitPrice (double unitPrice){
//        if (unitPrice <= 0) {
//            System.out.println("Unit price must be greater than zero.");
//            return false;
//        }
//        return true;
//    }

}