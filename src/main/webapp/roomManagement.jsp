<%@ page contentType="text/html;charset=UTF-8" %>

<html>

<head>

<title>Room Management</title>

<style>

body{
font-family:Arial;
background:#f2f2f2;
}

.box{
width:500px;
margin:auto;
background:white;
padding:30px;
margin-top:50px;
box-shadow:0 0 10px gray;
}

input{
width:100%;
padding:10px;
margin:10px 0;
}

button{
padding:10px;
background:#28a745;
color:white;
border:none;
width:100%;
}

</style>

</head>

<body>

<div class="box">

<h2>🏨 Add Room Category Type</h2>

<form action="RoomManagementServlet" method="post">

<label>Room Category</label>
<input type="text" name="category" placeholder="Deluxe / Suite / Standard" required>

<label>Room Type</label>
<input type="text" name="type" placeholder="Single / Double / Sea View" required>

<label>Room Price</label>
<input type="number" name="price" required>

<label>Room Count</label>
<input type="number" name="count" required>

<button type="submit">Create Rooms</button>

</form>

</div>

</body>

</html>