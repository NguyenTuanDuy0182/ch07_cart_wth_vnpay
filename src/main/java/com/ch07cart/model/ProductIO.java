package com.ch07cart;

import java.io.*;
import java.util.*;

public class ProductIO {

    public static Product getProduct(String code, InputStream is) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line = in.readLine();
            while (line != null) {
                StringTokenizer t = new StringTokenizer(line, "|");
                if (t.hasMoreTokens()) {
                    String productCode = t.nextToken();
                    if (code.equalsIgnoreCase(productCode)) {
                        String description = t.nextToken();
                        double price = Double.parseDouble(t.nextToken());
                        Product p = new Product();
                        p.setCode(code);
                        p.setDescription(description);
                        p.setPrice(price);
                        return p;
                    }
                }
                line = in.readLine();
            }
            return null;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public static ArrayList<Product> getProducts(String filepath) {
        ArrayList<Product> products = new ArrayList<Product>();
        File file = new File(filepath);
        try {
            BufferedReader in = new BufferedReader(
                    new FileReader(file));

            String line = in.readLine();
            while (line != null) {
                StringTokenizer t = new StringTokenizer(line, "|");
                String code = t.nextToken();
                String description = t.nextToken();
                String priceAsString = t.nextToken();
                double price = Double.parseDouble(priceAsString);
                Product p = new Product();
                p.setCode(code);
                p.setDescription(description);
                p.setPrice(price);
                products.add(p);
                line = in.readLine();
            }
            in.close();
            return products;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }
}