<%@page contentType="text/html" pageEncoding="utf-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <title>NgTDuy - Checkout</title>
                <link rel="stylesheet" href="style.css" type="text/css" />
                <style>
                    .customer-info-box {
                        background-color: #f9f9f9;
                        border: 1px solid #ddd;
                        padding: 10px 20px;
                        width: 50em;
                        margin-top: 20px;
                        margin-bottom: 20px;
                    }

                    .customer-info-box h3 {
                        color: teal;
                        margin-top: 0;
                        margin-bottom: 10px;
                    }

                    .info-row {
                        margin-bottom: 5px;
                    }

                    .bold {
                        font-weight: bold;
                    }

                    .payment-method {
                        margin-bottom: 20px;
                        display: flex;
                        align-items: center;
                        gap: 15px;
                    }

                    .action-buttons {
                        margin-top: 10px;
                    }

                    .action-buttons form {
                        display: inline-block;
                        margin-right: 10px;
                    }

                    .total-row {
                        margin-top: 15px;
                        font-size: 1.1em;
                    }
                </style>
            </head>

            <body>
                <h1>CheckOut</h1>

                <table>
                    <tr>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Amount</th>
                    </tr>

                    <c:set var="total" value="0" />
                    <c:forEach var="item" items="${cart.items}">
                        <tr>
                            <td>${item.product.description}</td>
                            <td>${item.product.priceCurrencyFormat}</td>
                            <td>${item.quantity}</td>
                            <td>${item.totalCurrencyFormat}</td>
                        </tr>
                        <c:set var="total" value="${total + item.total}" />
                    </c:forEach>
                </table>

                <div class="total-row">
                    <b>Total: </b>
                    <fmt:formatNumber value="${total}" type="currency" currencySymbol="VND" maxFractionDigits="0"
                        minFractionDigits="0" />
                </div>

                <div class="customer-info-box">
                    <h3 style="font-size: 140%; margin-bottom: .5em;">Customer Information</h3>
                    <div class="info-row">
                        <span class="bold">Username:</span> ${user.username}
                    </div>
                    <div class="info-row">
                        <span class="bold">Email to receive receipt:</span> ${user.email}
                    </div>
                </div>

                <div class="payment-method">
                    <span class="bold">Payment Method:</span> VNPay Sandbox Gateway
                </div>

                <div class="action-buttons" style="margin-bottom: 10px;">
                    <form action="payment" method="post">
                        <input type="hidden" name="amount" value="<fmt:formatNumber value='${total}' pattern='0' />">
                        <input type="hidden" name="orderInfo" value="Thanh toan don hang">
                        <input type="submit" value="Proceed to Payment">
                    </form>
                </div>

                <div class="action-buttons">
                    <form action="cart" method="post">
                        <input type="hidden" name="action" value="view">
                        <input type="submit" value="Back to Cart">
                    </form>

                    <form action="cart" method="post">
                        <input type="hidden" name="action" value="shop">
                        <input type="submit" value="Continue Shopping">
                    </form>
                </div>

            </body>

            </html>