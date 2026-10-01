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
        <p>To add items to your cart, please enter your name and email address below.</p>

        <form action="" method="post">
            <div class="form-row">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username">
            </div>
            <div class="form-row">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password">
            </div>
            <div class="form-row">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email">
            </div>
            <div class="form-row">
                <label></label>
                <input type="submit" value="Register">
            </div>
        </form>
    </body>

    </html>