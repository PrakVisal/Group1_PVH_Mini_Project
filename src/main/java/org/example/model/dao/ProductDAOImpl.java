package org.example.model.dao;

import org.example.config.DatabaseConnection;
import org.example.config.color.Color;
import org.example.model.Product;
import org.example.vaildate.Validate;
import org.example.view.ProductView;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class ProductDAOImpl implements ProductDAO {
    Scanner sc = new Scanner(System.in);

    private final DatabaseConnection databaseConnection;
    public ProductDAOImpl()
    {
        this.databaseConnection = new DatabaseConnection();
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
        String option = null;
        do {
            System.out.println("(si) for save insert \n(su) for save update \n(b) back");
            System.out.println("Enter option: ");
            option = sc.nextLine();
            switch (option){
                case "si":{
                    String query = "INSERT INTO product (product_name, quantity, unit_price, import_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
                    try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query)) {
                        for (Product p : productList) {
                            preparedStatement.setString(1, p.getName());
                            preparedStatement.setInt(2, p.getQuantity());
                            preparedStatement.setBigDecimal(3, new BigDecimal(p.getUnitPrice()));
                            preparedStatement.executeUpdate(); // Execute insert
                            System.out.println(Color.GREEN+"Inserted successfully"+Color.RESET);
                        }
                        productList.clear();
                    } catch (SQLException e) {
                        System.out.println("Error inserting products: " + e.getMessage());
                        throw e;
                    }
                    break;
                }
                case "su":{
                    //not done
                    break;
                }
                case "b":{
                    System.out.println(Color.YELLOW+"Exiting program..."+Color.RESET+"\n");
                    break;
                }
            }
        }while(!option.equalsIgnoreCase("b"));
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
                    ProductView.table(productInsert);
                    break;
                }
                case "uu":{
                    ProductView.table(productUpdate);
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
        LocalDate importDate = LocalDate.now();
        Scanner sc = new Scanner(System.in);
        int id;
        if(getAllProducts().isEmpty()){
            id =0;
        }else {
            id = getAllProducts().getLast().getId();
        }
        String name = null;
        double unitPrice = 0;
        int quantity = 0;
        System.out.println("ID: "+(++id));
        System.out.println("Enter Name: ");
        name = sc.nextLine();

        boolean validUnitPrice = false;
        while (!validUnitPrice) {
            System.out.println("Enter Unit Price: ");
            if (sc.hasNextDouble()) {
                unitPrice = sc.nextDouble();
                validUnitPrice = true;
            } else {
                System.out.println("Invalid input. Please enter a valid unit price (e.g., 2.5).");
                sc.next();
            }
        }
        sc.nextLine();
        boolean validQuantity = false;
        while (!validQuantity) {
            System.out.println("Enter Quantity: ");
            if (sc.hasNextInt()) {
                quantity = sc.nextInt();
                validQuantity = true;
            } else {
                System.out.println("Invalid input. Please enter a valid quantity (integer).");
                sc.next();
            }
        }
        if (Validate.validateProduct(id, name, quantity, unitPrice, importDate)) {
            Product newProduct = new Product(id, name, quantity, unitPrice, importDate);
            product.add(newProduct);
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Product validation failed. Please try again.");
        }
    }
    @Override
    public void updateProduct(List<Product> product) {
        ArrayList<String> renderUpdate = new ArrayList<>();
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
                product.add(new Product(id,name,quantity,unitPrice,importDate));
            }
            ProductView.table(product);
            product.clear();
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
                            LocalDate importDate = rs2.getDate(5).toLocalDate();
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
                            LocalDate importDate = rs3.getDate(5).toLocalDate();
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
                            LocalDate importDate = result.getDate(5).toLocalDate();
                            product.add(new Product(id,name4,qty,unitPrice,importDate));
                        }
                        break;
                    case 4:
                        System.out.print("Enter Name: ");
                        String name1 = cin.nextLine();
                        System.out.print("Enter Unit Price: ");
                        double uPrice1 = Double.parseDouble(cin.nextLine());
                        System.out.print("Enter Qty: ");
                        int qty1 = Integer.parseInt(cin.nextLine());
                        renderUpdate.add(String.valueOf(pm1.getId()));
                        renderUpdate.add(String.valueOf(name1));
                        renderUpdate.add(String.valueOf(uPrice1));
                        renderUpdate.add(String.valueOf(qty1));
                        renderUpdate.add(String.valueOf(pm1.getImportedDate()));
                        break;
                    default:
                        break;
                }
            }
//            unSaveUpdate();
        }catch (Exception ex){
            System.out.println("Error Update: " + ex);
        }
    }
    @Override
    public void deleteProduct() {
        System.out.println("Enter id to delete product");
        int idInputed = sc.nextInt();
        Product  product = getAllProducts().stream().filter( e -> e.getId() == idInputed).findFirst().orElse(null);
        String sql = "DELETE FROM product WHERE id = ?";
        try(PreparedStatement ps = databaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, product.getId());
            ps.executeUpdate();
            System.out.println("Delete product success");
        }catch (SQLException e){
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
                    System.out.println("No products found with the ID: " + productID);
                } else {
                    ProductView.table(matchedProductsID);
                    System.out.print("Press any key to continue...");
                    sc.nextLine();
                }

            } catch (SQLException e) {
                System.out.println("Error querying the database: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a valid number.");
        }
    }
    @Override
    public void searchProductbyName() {
        System.out.println("Enter name: ");
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
            System.out.println("Press any key to continue...");
            sc.nextLine();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
