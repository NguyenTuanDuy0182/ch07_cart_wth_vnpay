package com.ch07cart.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ProductIO {

    public static Product getProduct(String code, InputStream is) {
        if (is == null || code == null) {
            return null;
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    StringTokenizer t = new StringTokenizer(line, "|");
                    if (t.countTokens() >= 3) {
                        String productCode = t.nextToken().trim();
                        if (code.equalsIgnoreCase(productCode)) {
                            String description = t.nextToken().trim();
                            double price = Double.parseDouble(t.nextToken().trim());
                            Product p = new Product();
                            p.setCode(code);
                            p.setDescription(description);
                            p.setPrice(price);
                            return p;
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error reading product: " + e.getMessage());
            return null;
        }
    }

    public static ArrayList<Product> getProducts(InputStream is) {
        ArrayList<Product> products = new ArrayList<>();
        if (is == null) {
            return products;
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    StringTokenizer t = new StringTokenizer(line, "|");
                    if (t.countTokens() >= 3) {
                        String code = t.nextToken().trim();
                        String description = t.nextToken().trim();
                        String priceAsString = t.nextToken().trim();
                        double price = Double.parseDouble(priceAsString);
                        Product p = new Product();
                        p.setCode(code);
                        p.setDescription(description);
                        p.setPrice(price);
                        products.add(p);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading products: " + e.getMessage());
        }
        return products;
    }

    public static ArrayList<Product> getProducts(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            return getProducts(fis);
        } catch (IOException e) {
            System.err.println("Error reading products from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}