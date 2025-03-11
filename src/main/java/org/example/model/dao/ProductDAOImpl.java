package org.example.model.dao;

import org.example.config.DatabaseConnection;
import org.example.config.color.Color;
import org.example.controller.ProductController;
import org.example.model.Product;
import org.example.validate.Validate;
import org.example.view.ProductView;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.view.ProductView.table;

public class ProductDAOImpl implements ProductDAO {
    Scanner sc = new Scanner(System.in);
    private static int nextId = 1;

    private final DatabaseConnection databaseConnection;
    private Date importedDate;

    public ProductDAOImpl() {
        this.databaseConnection = new DatabaseConnection();
        nextId = getMaxIdFromDatabase() + 1;
    }

    private int getMaxIdFromDatabase() {
        int maxId = 0;
        try (Statement statement = databaseConnection.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT MAX(id) AS max_id FROM product");
            if (rs.next()) {
                maxId = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId;
    }

    public List<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();
        try(Statement statement = databaseConnection.getConnection().createStatement()){
            ResultSet rs = statement.executeQuery("select * from product");
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("unit_price");
                LocalDate importedDate = rs.getDate("import_date").toLocalDate();
                Product  product = new Product(id , name , quantity , price, importedDate);
                allProducts.add(product);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allProducts;
    }

    @Override
    public void saveProduct(List<Product> productListInsert, List<Product> productsListUpdate) throws SQLException {
        String option;
        do {
            System.out.println(Color.GREEN+"(Si) for save insert \t (Su) for save update \t (b) back"+Color.RESET);
            System.out.print("Enter option: ");
            option = sc.nextLine().trim().toLowerCase();

            switch (option) {
                case "si":
                    if (productListInsert == null || productListInsert.isEmpty()) {
                        System.out.println(Color.YELLOW+"No product inserted to the list."+Color.RESET);
                        break;
                    }

                    String query = "INSERT INTO product (product_name, quantity, unit_price, import_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
                    try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query)) {
                        for (Product p : productListInsert) {
                            preparedStatement.setString(1, p.getName());
                            preparedStatement.setInt(2, p.getQuantity());
                            preparedStatement.setBigDecimal(3, new BigDecimal(p.getUnitPrice()));
                            preparedStatement.executeUpdate(); // Execute insert
                        }
                        System.out.println(Color.GREEN+"Inserted successfully."+Color.RESET);
                        table(productListInsert);
                        productListInsert.clear();
                    } catch (SQLException e) {
                        System.out.println("Error inserting products: " + e.getMessage());
                        throw e;
                    }
                    break;

                case "su":
                    if (productsListUpdate == null || productsListUpdate.isEmpty()) {
                        System.out.println(Color.YELLOW+"No product updated to the list."+Color.RESET);
                        break;
                    }

                    String updateQuery = "UPDATE product SET product_name = ?, quantity = ?, unit_price = ?, import_date = ? WHERE id = ?";
                    try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(updateQuery)) {
                        for (Product p : productsListUpdate) {
                            preparedStatement.setString(1, p.getName());
                            preparedStatement.setInt(2, p.getQuantity());
                            preparedStatement.setBigDecimal(3, new BigDecimal(p.getUnitPrice()));
                            preparedStatement.setDate(4,Date.valueOf(p.getImportedDate()));
                            preparedStatement.setInt(5, p.getId());
                            preparedStatement.executeUpdate(); // Execute insert

                        }
                        System.out.println(Color.GREEN+"Updated successfully"+Color.RESET);
                        table(productsListUpdate);
                        productsListUpdate.clear();
                    } catch (SQLException e) {
                        System.out.println("Error updating products: " + e.getMessage());
                        throw e;
                    }
                    break;

                case "b":
                    System.out.println(Color.YELLOW + "Exiting program..." + Color.RESET + "\n");
                    break;
            }
        } while (!option.equalsIgnoreCase("b"));
    }

    @Override
    public void unSaveProduct(List<Product> productInsert,List<Product> productUpdate) {
        String option = null;
        do {
            System.out.println("(ui) for unsave insert \t (uu) for unsave update \t (b) back");
            System.out.println("Enter option: ");
            option = sc.nextLine().trim().toLowerCase();
            switch (option){
                case "ui":{
                    table(productInsert);
                    break;
                }
                case "uu":{
                    table(productUpdate);
                    break;
                }
                case "b":{
                    System.out.println("Exiting program....");
                    break;
                }
            }
        }while (!option.equalsIgnoreCase("b"));
    }

    @Override
    public void writeProduct(List<Product> product) {
        Scanner sc = new Scanner(System.in);
        LocalDate importDate = LocalDate.now();

        String response = "";
        while(!response.equals("no") && !response.equals("n")){
            System.out.println("ID: " + nextId);

            String name;
            do {
                System.out.print("Enter Name: ");
                name = sc.nextLine();
            } while (!Validate.validateName(name));

            double unitPrice = Validate.getValidUnitPrice(sc);
            int quantity = Validate.getValidQuantity(sc);

            if (Validate.validateProduct(nextId, name, quantity, unitPrice, importDate)) {
                Product newProduct = new Product(nextId, name, quantity, unitPrice, importDate);
                product.add(newProduct);
                System.out.println(Color.GREEN+"Product added successfully."+Color.RESET);
                nextId++;
            } else {
                System.out.println(Color.RED+"Product validation failed. Please try again."+Color.RESET);
            }

            sc.nextLine();
            System.out.print(Color.PURPLE+"Press Enter to continue or type 'no' to stop: "+Color.RESET);
            response = sc.nextLine().trim().toLowerCase();
        }
        System.out.println(Color.YELLOW+"Exiting product entry..."+Color.RESET);
    }

    @Override
    public void updateProduct(List<Product> product) {
        ArrayList<Product> searchRowforShow = new ArrayList<>();
        Scanner cin = new Scanner(System.in);
        Product pm1 = new Product();
        String showUpdate = "SELECT id,product_name,quantity,to_char(unit_price,'FM$9999990.00') AS unit_price,current_date AS importDate FROM product WHERE id = ?";

        try(PreparedStatement ps = databaseConnection.getConnection().prepareStatement(showUpdate)){
            System.out.print("Input ID to update: ");
            pm1.setId(Integer.parseInt(cin.nextLine()));
            ps.setInt(1,pm1.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int quantity = rs.getInt(3);
                double unitPrice = rs.getDouble(4);
                LocalDate importDate = rs.getDate(5).toLocalDate();
                searchRowforShow.add(new Product(id,name,quantity,unitPrice,importDate));
            }
            table(searchRowforShow);
            searchRowforShow.clear();
            System.out.println("1. Name \t 2. Unit Price \t 3. Qty \t 4. All Field \t 5. Exit");
            int op=0;
            while (op!=5){
                System.out.print("Choose an option to Update: ");
                op = Integer.parseInt(cin.nextLine());
                switch (op){
                    case 1:
                        System.out.print("Enter Name: ");
                        String name = cin.nextLine();
                        ResultSet rs2 = ps.executeQuery();
                        while (rs2.next()){
                            int id = rs2.getInt(1);
                            int quantity = rs2.getInt(3);
                            double unitPrice = rs2.getDouble(4);
                            LocalDate importDate = LocalDate.now();
                            product.add(new Product(id,name,quantity,unitPrice,importDate));
                        }
                        break;
                    case 2:
                        System.out.print("Enter Unit Price: ");
                        double uPrice = Double.parseDouble(cin.nextLine());
                        ResultSet rs3 = ps.executeQuery();
                        while (rs3.next()){
                            int id = rs3.getInt(1);
                            String name3 = rs3.getString(2);
                            int quantity = rs3.getInt(3);
                            LocalDate importDate = LocalDate.now();
                            product.add(new Product(id,name3,quantity,uPrice,importDate));
                        }
                        break;
                    case 3:
                        System.out.print("Enter Qty: ");
                        int qty = Integer.parseInt(cin.nextLine());
                        ResultSet result = ps.executeQuery();
                        while (result.next()){
                            int id = result.getInt(1);
                            String name4 = result.getString(2);
                            double unitPrice = result.getDouble(4);
                            LocalDate importDate = LocalDate.now();
                            product.add(new Product(id,name4,qty,unitPrice,importDate));
                        }
                        break;
                    case 4:
                        System.out.print("Enter Name: ");
                        String name5 = cin.nextLine();
                        System.out.print("Enter Unit Price: ");
                        int uPrice5 = Integer.parseInt(cin.nextLine());
                        System.out.print("Enter Qty: ");
                        double qty5 = Double.parseDouble(cin.nextLine());
                        ResultSet rs4 = ps.executeQuery();
                        while (rs4.next()){
                            int id = rs4.getInt(1);
                            LocalDate importDate5 = LocalDate.now();
                            product.add(new Product(id,name5,uPrice5,qty5,importDate5));
                        }
                        break;
                    default:{
                        System.out.println("Please enter a valid option");
                        break;
                    }
                }
            }
        }catch (Exception ex){
            System.out.println("Error Update: " + ex);
        }
    }


    @Override
    public void deleteProduct() {
        int idInputed = 0;
        boolean validId = false;

        while (!validId) {
            System.out.print("Enter product ID: ");
            String input = sc.nextLine();

            try {
                idInputed = Integer.parseInt(input);
                validId = Validate.validateProductId(idInputed);
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Invalid input. Please enter a valid numeric product ID." + Color.RESET);
            }
        }
        int finalIdInputed = idInputed;
        Product product = getAllProducts().stream()
                .filter(e -> e.getId() == finalIdInputed)
                .findFirst()
                .orElse(null);

        if (product == null) {
            System.out.println(Color.RED+"Product with ID " + idInputed + " not found."+Color.RESET);
            return;
        }

        table(Collections.singletonList(product));

        System.out.print("Are you sure you want to delete product ID: " + idInputed + "? (y/n): ");
        String deleteChoice = sc.nextLine().trim().toLowerCase();

        if (deleteChoice.equals("n")) {
            System.out.println("Product deletion canceled.");
            return;
        }
        String sql = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement ps = databaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, product.getId());
            ps.executeUpdate();
            System.out.println(Color.PURPLE+"Product deleted successfully."+Color.RESET);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void searchProductbyID() {
        System.out.print("Enter product ID: ");
        String inputID = sc.nextLine().trim();

        try {
            int productID = Integer.parseInt(inputID);
            String query = "SELECT * FROM product WHERE id = ?";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, productID);
                ResultSet resultSet = preparedStatement.executeQuery();

                List<Product> matchedProductsID = new ArrayList<>();

                while (resultSet.next()) {

                    Product product = new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("product_name"),
                            resultSet.getInt("quantity"),
                            resultSet.getDouble("unit_price"),
                            resultSet.getDate("import_date").toLocalDate()
                    );
                    matchedProductsID.add(product);

                }

                if (matchedProductsID.isEmpty()) {
                    System.out.println(Color.RED+"No products found with the ID: " + productID+Color.RESET);
                } else {
                    table(matchedProductsID);

                }

            } catch (SQLException e) {
                System.out.println(Color.RED+"Error querying the database: " + e.getMessage()+Color.RESET);
            }

        } catch (NumberFormatException e) {
            System.out.println(Color.RED+"Invalid ID. Please enter a valid number."+Color.RESET);
        }

        System.out.println(Color.YELLOW+"Press any key to continue..."+Color.RESET);
        sc.nextLine();
    }


    @Override
    public void searchProductbyName() {
        System.out.print("Enter name: ");
        String inputName = sc.nextLine();
        String queryAlike = "SELECT * FROM product WHERE product_name LIKE ?";
        List<Product> foundData = new ArrayList<>();
        try(PreparedStatement ps = databaseConnection.getConnection().prepareStatement(queryAlike)){
            ps.setString(1, "%"+inputName+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double unitPrice = rs.getDouble("unit_price");
                LocalDate importDate = rs.getDate("import_date").toLocalDate();
                foundData.add(new Product(id,name,quantity,unitPrice,importDate));

            }
            table(foundData);
            System.out.println(Color.YELLOW+"Press any key to continue..."+Color.RESET);
            sc.nextLine();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
