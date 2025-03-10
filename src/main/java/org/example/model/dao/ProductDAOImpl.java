package org.example.model.dao;

import org.example.config.DatabaseConnection;
import org.example.config.color.Color;
import org.example.model.Product;
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
                            System.out.println("Inserted successfully");
                        }
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
    public void unSaveProduct(List<Product> product) {
        String option = null;
        do {
            System.out.println("(ui) for unsave insert \t (uu) for unsave update \t (b) back");
            System.out.println("Enter option: ");
            option = sc.nextLine().trim().toLowerCase();
            switch (option){
                case "ui":{
                    System.out.println("data:"+product);
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
        int id = getAllProducts().getLast().getId();;
        String name = null;
        double unitPrice = 0;
        int quantity = 0;
        System.out.println("ID: "+(++id));
        System.out.println("Enter Name: ");
        name = sc.nextLine();
        System.out.println("Enter UnitPrice: ");
        unitPrice = sc.nextDouble();
        System.out.println("Enter Quantity: ");
        quantity = sc.nextInt();
        Product products = new Product(id,name,quantity,unitPrice,LocalDate.now());
        product.add(products);
    }
    @Override
    public void updateProduct(List<Product> product) {

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
            System.out.println("Enter id: ");
            int inputId = sc.nextInt();
               getAllProducts().stream().filter(product -> product.getId() == inputId).forEach(product -> {
                   System.out.println(product);
               });
    }

    @Override
    public void searchProductbyName() {
        System.out.println("Enter name: ");
        String inputName = sc.nextLine();
        getAllProducts().stream().filter(searchedProduct -> searchedProduct.getName().equalsIgnoreCase(inputName)).forEach(data-> System.out.println(data));
    }
}
