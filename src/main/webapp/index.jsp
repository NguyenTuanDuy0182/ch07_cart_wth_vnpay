<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.List, java.io.InputStream, com.ch07cart.model.Product, com.ch07cart.model.ProductIO" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <% InputStream is=application.getResourceAsStream("/WEB-INF/products.txt"); List<Product> products =
                ProductIO.getProducts(is);
                request.setAttribute("products", products);
                %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>NgTDuy - CD Shop</title>
                    <link rel="stylesheet" href="style.css">
                </head>

                <body>
                    <h1>CD list</h1>
                    <table>
                        <tr>
                            <th>Description</th>
                            <th class="right">Price</th>
                            <th>&nbsp;</th>
                        </tr>
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td>
                                    <c:out value="${product.description}" />
                                </td>
                                <td class="right">
                                    <c:out value="${product.priceCurrencyFormat}" />
                                </td>
                                <td>
                                    <form action="cart" method="post">
                                        <input type="hidden" name="action" value="cart">
                                        <input type="hidden" name="productCode" value="${product.code}">
                                        <input type="submit" value="Add To Cart">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </body>

                </html>