<%@page contentType="text/html" pageEncoding="utf-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="utf-8">
        <title>User Registration</title>
        <link rel="stylesheet" href="style.css" type="text/css" />
    </head>

    <body>
        <h1>User Registration</h1>
        <p>Please enter your username and email address to proceed to checkout.</p>

        <form action="cart" method="post">
            <input type="hidden" name="action" value="register">
            <div class="form-row">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-row">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password">
            </div>
            <div class="form-row">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-row">
                <label></label>
                <input type="submit" value="Register & Checkout">
            </div>
        </form>

        <form action="cart" method="post">
            <input type="hidden" name="action" value="view">
            <div class="form-row">
                <label></label>
                <input type="submit" value="Return to Cart">
            </div>
        </form>
    </body>

    </html>