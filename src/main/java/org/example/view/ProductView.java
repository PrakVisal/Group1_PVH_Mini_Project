package org.example.view;

import org.example.config.color.Color;
import org.example.controller.ProductController;
import org.example.model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductView {
    public static void displayProduct()
    {
        String option = null;
        Scanner sc = new Scanner(System.in);
        ProductController productController = new ProductController();
        ArrayList<Product> listUnsavedInsert = new ArrayList<>();
        ArrayList<Product> listUnsavedUpdate = new ArrayList<>();

        try{
            do {
                List<Product> productList = productController.getAllProducts();
                if(!productList.isEmpty()){
                    table(productList);
                }
                System.out.println(Color.CYAN+"(W).Write \t (R).Read(id) \t (U).Update \t (D).Delete \t (S).Search(name) \t (Se).Set row"+Color.RESET);
                System.out.println(Color.CYAN+"(Sa).Save \t (Us).Unsaved \t (Ba).Backup \t (Re).Restore \t (E)Exit"+Color.RESET);
                System.out.print("Choose an option:");
                option = sc.nextLine().trim().toLowerCase();

                switch (option){
                    case "w":{
                        productController.writeProduct(listUnsavedInsert);
                        break;
                    }
                    case "us":{
                        productController.unSaveProduct(listUnsavedInsert,listUnsavedUpdate);
                        break;
                    }
                    case "sa":{
                        productController.saveProduct(listUnsavedInsert,listUnsavedUpdate);
                        break;
                    }
                    case "r":{
                        productController.seachProductbyID();
                        break;
                    }
                    case "d":{
                        productController.deleteProductById();
                        break;
                    }
                    case "s":{
                        productController.searchProductByName();
                        break;
                    }
                    case "u":{
                        productController.updateData(listUnsavedUpdate);
                        break;
                    }
                    case "e":{
                        System.out.println(Color.RED+"Exiting..."+Color.RESET);
                        break;
                    }
                }
            }while (!option.equalsIgnoreCase("e"));
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            sc.close();
        }
    }
     public static void table(List<Product> productData)  {
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
        Table t = new Table(5, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);

        t.setColumnWidth(0, 10, 10);
        t.setColumnWidth(1, 30, 30);
        t.setColumnWidth(2, 30, 30);
        t.setColumnWidth(3, 20, 20);
        t.setColumnWidth(4, 30, 30);

        t.addCell(Color.PURPLE + "ID" + Color.RESET, cellStyle);
        t.addCell(Color.PURPLE + "NAME" + Color.RESET, cellStyle);
        t.addCell(Color.PURPLE + "QTY" + Color.RESET, cellStyle);
        t.addCell(Color.PURPLE + "UNIT PRICE" + Color.RESET, cellStyle);
        t.addCell(Color.PURPLE + "IMPORT DATE" + Color.RESET, cellStyle);


        for(Product product : productData){
            t.addCell(Color.YELLOW + product.getId() + Color.RESET, cellStyle);
            t.addCell(Color.YELLOW + product.getName() + Color.RESET, cellStyle);
            t.addCell(Color.YELLOW + product.getQuantity() + Color.RESET, cellStyle);
            t.addCell(Color.YELLOW + product.getUnitPrice() + Color.RESET, cellStyle);
            t.addCell(Color.YELLOW + product.getImportedDate() + Color.RESET, cellStyle);
        }
         System.out.println(t.render());

    }
}
