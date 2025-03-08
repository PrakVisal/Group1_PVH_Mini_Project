package org.example.controller;


import org.example.model.Product;
import org.example.model.dao.ProductDAOImpl;

import java.sql.SQLException;
import java.util.List;

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
}

