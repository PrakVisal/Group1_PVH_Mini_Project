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
    public void seachProductbyID(){
        productDAO.searchProductbyID();
    }
    public List<Product> getAllProducts(){
        return productDAO.getAllProducts();
    }
    public void deleteProductById(){
        productDAO.deleteProduct();
    }
    public void searchProductByName(){
        productDAO.searchProductbyName();
    }
    public void updateData(List<Product> product){
        productDAO.updateProduct(product);
    }
}

