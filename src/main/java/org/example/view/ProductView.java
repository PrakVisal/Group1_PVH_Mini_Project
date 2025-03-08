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
    public static void displayProduct() {
        try {
            String option = null;
            Scanner sc = new Scanner(System.in);
            ProductController productController = new ProductController();
            ArrayList<Product> listUnsaved = new ArrayList<>();

            int page = 1;
            int pageSize = 3;
            int totalPages = 0;

            do {
                List<Product> productList = productController.getAllProducts();

                totalPages = (int) Math.ceil((double) productList.size() / pageSize);

                List<Product> paginatedProducts = getPaginatedProducts(productList, page, pageSize);
                table(paginatedProducts);

                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t---------- Menu ----------");
                System.out.println("\t\tN. Next Page \t\t P. Previous Page \t\t F. First Page \t\t L. Last Page \t\t G. Goto\n");

                System.out.println("(W).Write \t (R).Read(id) \t (U).Update \t (D).Delete \t (S).Search(name) \t (Se).Set row");
                System.out.println("(Sa).Save \t (Us).Unsaved \t (Ba).Backup \t (Re).Restore \t (E)Exit");
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t ------------------------\n");
                System.out.print("Choose an option: ");
                option = sc.nextLine().trim().toLowerCase();

                switch (option) {
                    case "w": {
                        productController.writeProduct(listUnsaved);
                        break;
                    }
                    case "us":
                        productController.unSaveProduct(listUnsaved);
                        break;

                    case "0":
                        listUnsaved.forEach(data -> {
                            System.out.println(data.getName() + "\t" + data.getUnitPrice() + "\t" + data.getQuantity());
                        });
                        break;

                    case "sa":
                        productController.saveProduct(listUnsaved);
                        break;

                    case "r":
                        productController.seachProductbyID();
                        break;

                    case "d":
                        productController.deleteProductById();
                        break;

                    case "s":

                        break;

                    case "n":
                        if (page < totalPages) {
                            page++;
                        } else {
                            System.out.println("already on the last page.");
                        }
                        break;

                    case "p":
                        if (page > 1) {
                            page--;
                        } else {
                            System.out.println("already on the first page.");
                        }
                        break;

                    case "f":
                        page = 1;
                        break;

                    case "l":
                        page = totalPages;
                        break;

                    case "g":
                        System.out.print("Enter page number: ");
                        int gotoPage = sc.nextInt();
                        sc.nextLine();
                        if (gotoPage >= 1 && gotoPage <= totalPages) {
                            page = gotoPage;
                        } else {
                            System.out.println("Invalid page number.");
                        }
                        break;

                    case "e":
                        System.out.print("Exiting...");
                        break;

                    default:
                        System.out.print("Invalid option");
                }
            } while (!option.equalsIgnoreCase("e"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static List<Product> getPaginatedProducts(List<Product> productList, int page, int pageSize) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, productList.size());
        return productList.subList(startIndex, endIndex);
    }

     public static void table(List<Product> productData) {
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
        Table t = new Table(5, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);

         int page = 1;
         int pageSize = 3;

         int totalPages = (int) Math.ceil((double) productData.size() / pageSize);

        t.setColumnWidth(0, 10, 10);
        t.setColumnWidth(1, 30, 30);
        t.setColumnWidth(2, 30, 30);
        t.setColumnWidth(3, 20, 20);
        t.setColumnWidth(4, 30, 30);

        t.addCell(Color.PURPLE + "ID" + Color.RESET, cellStyle);
        t.addCell(Color.PURPLE + "NAME" + Color.RESET, cellStyle);
        t.addCell(Color.PURPLE + "UNIT PRICE" + Color.RESET, cellStyle);
        t.addCell(Color.PURPLE + "QTY" + Color.RESET, cellStyle);
        t.addCell(Color.PURPLE + "IMPORT DATE" + Color.RESET, cellStyle);

        for(Product product : productData){
            t.addCell(Color.YELLOW + product.getId() + Color.RESET, cellStyle);
            t.addCell(Color.YELLOW + product.getName() + Color.RESET, cellStyle);
            t.addCell(Color.YELLOW + product.getQuantity() + Color.RESET, cellStyle);
            t.addCell(Color.YELLOW + product.getUnitPrice() + Color.RESET, cellStyle);
            t.addCell(Color.YELLOW + product.getImportedDate() + Color.RESET, cellStyle);
        }

         t.addCell(Color.PURPLE + "Page : " + page + " of " + totalPages + Color.RESET, cellStyle, 3);
         t.addCell(Color.PURPLE + "Total Record : " + productData.size() + Color.RESET, cellStyle, 3);
         System.out.println(t.render());

    }
}
