<%@page contentType="text/html" pageEncoding="utf-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="utf-8">
            <title>NgTDuy - Cart</title>
            <link rel="stylesheet" href="style.css" type="text/css" />
        </head>

        <body>

            <h1>Your cart</h1>
            <c:choose>
                <c:when test="${empty cart.items}">
                    <p>Your cart is empty.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <tr>
                            <th>Quantity</th>
                            <th>Description</th>
                            <th>Price</th>
                            <th>Amount</th>
                            <th></th>
                        </tr>

                        <c:forEach var="item" items="${cart.items}">
                            <tr>
                                <td>
                                    <form action="cart" method="post">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="productCode" value="${item.product.code}">
                                        <input type="text" name="quantity" value="${item.quantity}" id="quantity">
                                        <input type="submit" value="Update">
                                    </form>
                                </td>
                                <td>${item.product.description}</td>
                                <td>${item.product.priceCurrencyFormat}</td>
                                <td>${item.totalCurrencyFormat}</td>
                                <td>
                                    <form action="cart" method="post">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="productCode" value="${item.product.code}">
                                        <input type="hidden" name="quantity" value="0">
                                        <input type="submit" value="Remove Item">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <p><b>To change the quantity</b>, enter the new quantity
                        and click on the Update button.</p>
                </c:otherwise>
            </c:choose>

            <div style="margin-top: 15px; display: flex; gap: 10px;">
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="shop">
                    <input type="submit" value="Continue Shopping">
                </form>

                <c:if test="${not empty cart.items}">
                    <form action="cart" method="post">
                        <input type="hidden" name="action" value="checkout">
                        <input type="submit" value="Checkout">
                    </form>
                </c:if>
            </div>

        </body>

        </html>