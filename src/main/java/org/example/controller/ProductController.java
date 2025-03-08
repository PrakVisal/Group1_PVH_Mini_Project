package org.example.controller;


import org.example.model.Product;
import org.example.model.dao.ProductDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class ProductController {
    ProductDAOImpl productDAO = new ProductDAOImpl();
    public void saveProduct(List<Product> productInsert,List<Product> productUpdate) throws SQLException {
        productDAO.saveProduct(productInsert,productUpdate);
    }
    public void writeProduct(List<Product> product){
        productDAO.writeProduct(product);
    }
    public void unSaveProduct(List<Product> productInsert,List<Product>productUpdate) throws SQLException {
        productDAO.unSaveProduct(productInsert,productUpdate);
    }
    public void searchProductbyID() throws SQLException {
        productDAO.searchProductbyID();
    }
    public void searchProductbyName() throws SQLException {
        productDAO.searchProductbyName();
    }
    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }
    public void deleteProductById() throws SQLException {
        productDAO.deleteProduct();
    }
    public void updateProduct(List<Product> product) throws SQLException {
        productDAO.updateProduct(product);
    }
}

