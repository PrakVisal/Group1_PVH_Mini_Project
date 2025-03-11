package org.example;

import org.example.controller.ProductController;
import org.example.model.Product;
import org.example.model.dao.ProductDAOImpl;
import org.example.view.ProductView;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductView.displayProduct();
    }
}