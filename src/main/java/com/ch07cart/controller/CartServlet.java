package com.ch07cart.controller;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import com.ch07cart.model.*;

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

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String url = "/index.jsp";
        ServletContext sc = getServletContext();

        // get current action
        String action = request.getParameter("action");
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String[] actions = request.getParameterValues("action");
            if (actions != null && actions.length > 1) {
                // If query string had action=view but form body submitted a specific action,
                // prioritize the body parameter (non-"view")
                for (int i = actions.length - 1; i >= 0; i--) {
                    if (!"view".equalsIgnoreCase(actions[i])) {
                        action = actions[i];
                        break;
                    }
                }
            }
        }

        if (action == null || action.trim().isEmpty()) {
            if (request.getParameter("productCode") != null) {
                action = "cart";
            } else {
                action = "view";
            }
        }

        // perform action and set URL to appropriate page
        if (action.equals("shop")) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
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
            if (is != null && productCode != null) {
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
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null || cart.getItems().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }
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
            user.setUsername(username != null ? username.trim() : "");
            user.setEmail(email != null ? email.trim() : "");
            session.setAttribute("user", user);

            url = "/checkout.jsp";
        } else if (action.equals("update")) {
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