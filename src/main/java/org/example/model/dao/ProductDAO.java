package org.example.model.dao;

import org.example.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    public void writeProduct(List<Product> product) throws SQLException;
    public void unSaveProduct(List<Product> product);
    public void saveProduct(List<Product> product) throws SQLException;
    public void updateProduct(List<Product> product);
    public void deleteProduct();
    public void seachProductbyID();
    public List<Product> getAllProducts();

}
