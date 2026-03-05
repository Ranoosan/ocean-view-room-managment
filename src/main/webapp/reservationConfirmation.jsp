<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reservation Confirmed</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            padding: 20px;
        }
        
        .confirmation-card {
            background: white;
            border-radius: 15px;
            padding: 40px;
            max-width: 500px;
            width: 100%;
            text-align: center;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
        }
        
        .success-icon {
            width: 80px;
            height: 80px;
            background: #28a745;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 30px;
            color: white;
            font-size: 40px;
        }
        
        h1 {
            color: #333;
            margin-bottom: 20px;
        }
        
        .reservation-number {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin: 20px 0;
            font-size: 1.2em;
        }
        
        .reservation-number strong {
            color: #667eea;
            font-size: 1.5em;
            display: block;
            margin-top: 5px;
        }
        
        .details {
            text-align: left;
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
        }
        
        .details h3 {
            color: #333;
            margin-bottom: 15px;
        }
        
        .details p {
            margin: 10px 0;
            color: #666;
        }
        
        .btn {
            display: inline-block;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            text-decoration: none;
            border-radius: 5px;
            font-size: 1.1em;
            margin-top: 20px;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background: #6c757d;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <div class="confirmation-card">
        <div class="success-icon">✓</div>
        <h1>Booking Confirmed!</h1>
        <p>Your reservation has been successfully created.</p>
        
        <div class="reservation-number">
            Reservation Number:
            <strong><%= request.getAttribute("reservationNumber") %></strong>
        </div>
        
        <div class="details">
            <h3>📋 What's Next?</h3>
            <p>✓ Please save your reservation number for reference</p>
            <p>✓ Present valid ID proof at check-in</p>
            <p>✓ Check-in time: 2:00 PM</p>
            <p>✓ Check-out time: 12:00 PM</p>
        </div>
        
        <div>
            <a href="addReservation" class="btn">Make Another Reservation</a>
            <a href="viewReservations" class="btn btn-secondary">View All Reservations</a>
        </div>
    </div>
</body>
</html>