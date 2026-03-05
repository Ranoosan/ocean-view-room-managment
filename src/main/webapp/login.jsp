<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Ocean View Resort</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            background-color: #eef2f3; 
            display: flex; 
            justify-content: center; 
            align-items: center; 
            height: 100vh; 
            margin: 0;
        }
        .login-container { 
            background-color: #fff; 
            padding: 30px; 
            border-radius: 8px; 
            box-shadow: 0 0 10px rgba(0,0,0,0.2); 
            width: 350px;
        }
        h2 { text-align: center; color: #0077cc; }
        label { display: block; margin-top: 15px; }
        input[type=text], input[type=password] { 
            width: 100%; 
            padding: 8px; 
            margin-top: 5px; 
            border-radius: 4px; 
            border: 1px solid #ccc; 
        }
        button { 
            width: 100%; 
            padding: 10px; 
            margin-top: 20px; 
            border: none; 
            border-radius: 4px; 
            background-color: #0077cc; 
            color: white; 
            font-size: 16px;
            cursor: pointer;
        }
        button:hover { background-color: #005fa3; }
        .error { color: red; margin-top: 10px; text-align: center; }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Ocean View Resort Login</h2>
        <form action="login" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Login</button>

            <div class="error">
                <%
                    String error = (String) request.getAttribute("errorMessage");
                    if (error != null) {
                        out.print(error);
                    }
                %>
            </div>
        </form>
    </div>
</body>
</html>