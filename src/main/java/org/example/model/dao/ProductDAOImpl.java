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
    public void saveProduct(List<Product> productList) throws SQLException {
        String option;
        do {
            System.out.println("(Si) for save insert \t (Su) for save update \t (b) back");
            System.out.print("Enter option: ");
            option = sc.nextLine().trim().toLowerCase();

            switch (option) {
                case "si":
                    if (productList == null || productList.isEmpty()) {
                        System.out.println(Color.YELLOW+"No product inserted to the list."+Color.RESET);
                        break;
                    }

                    String query = "INSERT INTO product (product_name, quantity, unit_price, import_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
                    try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query)) {
                        for (Product p : productList) {
                            preparedStatement.setString(1, p.getName());
                            preparedStatement.setInt(2, p.getQuantity());
                            preparedStatement.setBigDecimal(3, new BigDecimal(p.getUnitPrice()));
                            preparedStatement.executeUpdate(); // Execute insert
                        }
                        System.out.println("Inserted successfully.");
                        table(productList);
                    } catch (SQLException e) {
                        System.out.println("Error inserting products: " + e.getMessage());
                        throw e;
                    }
                    break;

                case "su":


                    break;

                case "b":
                    System.out.println(Color.YELLOW + "Exiting program..." + Color.RESET + "\n");
                    break;
            }
        } while (!option.equalsIgnoreCase("b"));
    }

    @Override
    public void unSaveProduct(List<Product> product) {
        String option = null;
        do {
            System.out.println("(ui) for unsave insert \t (uu) for unsave update \t (b) back");
            System.out.print("Enter option: ");
            option = sc.nextLine().trim().toLowerCase();
            switch (option){
                case "ui":{
                    table(product);
                    System.out.println(Color.YELLOW+"Enter to continue..."+Color.RESET);
                    sc.nextLine();
                    break;
                }
                case "uu":{
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
        Scanner cin = new Scanner(System.in);
        String showUpdate = "SELECT id, product_name, quantity, unit_price, current_date AS importDate FROM product WHERE id = ?";

        try (PreparedStatement ps = databaseConnection.getConnection().prepareStatement(showUpdate)) {
            System.out.print("Input ID to update: ");
            int idInput = Integer.parseInt(cin.nextLine());
            ps.setInt(1, idInput);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int quantity = rs.getInt(3);
                double unitPrice = rs.getDouble(4);
                LocalDate importDate = rs.getDate(5).toLocalDate();

                Product existingProduct = new Product(id, name, quantity, unitPrice, importDate);
                product.add(existingProduct);
                table(product);
                product.clear();

                System.out.println("1. Name \t 2. Unit Price \t 3. Quantity \t 4. All Fields \t 5. Exit");
                int op = 0;
                while (op != 5) {
                    System.out.print("Choose an option to Update: ");
                    op = Integer.parseInt(cin.nextLine());
                    switch (op) {
                        case 1:
                            System.out.print("Enter new Name: ");
                            String newName = cin.nextLine();
                            existingProduct.setName(newName);
                            break;

                        case 2:
                            System.out.print("Enter new Unit Price: ");
                            double newUnitPrice = Double.parseDouble(cin.nextLine());
                            existingProduct.setUnitPrice(newUnitPrice);
                            break;

                        case 3:
                            System.out.print("Enter new Quantity: ");
                            int newQuantity = Integer.parseInt(cin.nextLine());
                            existingProduct.setQuantity(newQuantity);
                            break;

                        case 4:
                            System.out.print("Enter new Name: ");
                            String newNameAll = cin.nextLine();
                            System.out.print("Enter new Unit Price: ");
                            double newUnitPriceAll = Double.parseDouble(cin.nextLine());
                            System.out.print("Enter new Quantity: ");
                            int newQuantityAll = Integer.parseInt(cin.nextLine());

                            existingProduct.setName(newNameAll);
                            existingProduct.setUnitPrice(newUnitPriceAll);
                            existingProduct.setQuantity(newQuantityAll);
                            break;

                        default:
                            break;
                    }
                }

                if (existingProduct.getImportedDate() == null) {
                    existingProduct.setImportedDate(LocalDate.now());
                }

                String updateQuery = "UPDATE product SET product_name = ?, quantity = ?, unit_price = ?, import_date = ? WHERE id = ?";
                try (PreparedStatement updatePs = databaseConnection.getConnection().prepareStatement(updateQuery)) {
                    updatePs.setString(1, existingProduct.getName());
                    updatePs.setInt(2, existingProduct.getQuantity());
                    updatePs.setBigDecimal(3, new BigDecimal(existingProduct.getUnitPrice()));
                    updatePs.setDate(4, Date.valueOf(existingProduct.getImportedDate()));
                    updatePs.setInt(5, existingProduct.getId());

                    int rowsUpdated = updatePs.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Product updated successfully.");
                    } else {
                        System.out.println("Failed to update the product.");
                    }
                }
            } else {
                System.out.println("No product found with ID: " + idInput);
            }
        } catch (SQLException ex) {
            System.out.println("Error updating product: " + ex.getMessage());
        }
    }



    @Override
    public void deleteProduct() {
        System.out.print("Enter id to delete product: ");
        int idInputed = sc.nextInt();
        sc.nextLine();

        Product product = getAllProducts().stream()
                .filter(e -> e.getId() == idInputed)
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
            ProductView.table(foundData);
            System.out.println(Color.YELLOW+"Press any key to continue..."+Color.RESET);
            sc.nextLine();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
