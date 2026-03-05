<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Ocean View Resort</title>
</head>
<body>

<h2>Welcome, <%= username %> (<%= role %>)</h2>

<a href="logout">Logout</a>

<hr>

<h3>Hotel Management Menu</h3>

<ul>
    <li>
        <a href="addReservation.jsp">➕ Add New Reservation</a>
    </li>

    <li>
        <a href="roomManagement.jsp">🏨 Manage Rooms</a>
    </li>

    <li>
        <a href="viewReservations.jsp">📋 View Reservations</a>
    </li>

    <li>
        <a href="bills.jsp">💳 Generate Bills</a>
    </li>
</ul>

<p>Select an option to manage reservations, guests, or view reports.</p>

</body>
</html>