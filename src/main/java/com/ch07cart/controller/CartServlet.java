package com.ch07cart;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/index.jsp";
        ServletContext sc = getServletContext();

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "cart"; // default action
        }

        // perform action and set URL to appropriate page
        if (action.equals("shop")) {
            url = "/index.jsp"; // the "index" page
        } else if (action.equals("cart")) {
            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
            }

            // if the user enters a negative or invalid quantity,
            // the quantity is automatically reset to 1.
            int quantity;
            try {
                quantity = Integer.parseInt(quantityString);
                if (quantity < 0) {
                    quantity = 1;
                }
            } catch (NumberFormatException nfe) {
                quantity = 1;
            }

            InputStream is = sc.getResourceAsStream("/WEB-INF/products.txt");
            Product product = null;
            if (is != null) {
                product = ProductIO.getProduct(productCode, is);
            }

            if (product != null) {
                LineItem lineItem = new LineItem();
                lineItem.setProduct(product);
                lineItem.setQuantity(quantity);
                if (quantity > 0) {
                    cart.addItem(lineItem);
                } else if (quantity == 0) {
                    cart.removeItem(lineItem);
                }
            }

            session.setAttribute("cart", cart);
            // PRG Pattern: redirect after POST to avoid double submission
            response.sendRedirect(request.getContextPath() + "/cart?action=view");
            return;
        } else if (action.equals("view")) {
            url = "/cart.jsp";
        } else if (action.equals("checkout")) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                url = "/register.jsp";
            } else {
                url = "/checkout.jsp";
            }
        } else if (action.equals("register")) {
            String username = request.getParameter("username");
            String email = request.getParameter("email");

            HttpSession session = request.getSession();
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            session.setAttribute("user", user);

            url = "/checkout.jsp";
        }

        else if (action.equals("update")) {
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");

            String productCode = request.getParameter("productCode");
            if (productCode == null) {
                productCode = request.getParameter("code");
            }
            String quantityString = request.getParameter("quantity");

            if (cart != null && productCode != null) {
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityString);
                } catch (NumberFormatException nfe) {
                    quantity = 1;
                }
                cart.update(productCode, quantity);
                session.setAttribute("cart", cart);
            }
            response.sendRedirect(request.getContextPath() + "/cart?action=view");
            return;
        }

        sc.getRequestDispatcher(url).forward(request, response);
    }
}