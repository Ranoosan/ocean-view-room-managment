<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Room" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hotel Reservation System</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        
        .header {
            background: white;
            padding: 30px;
            border-radius: 10px 10px 0 0;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .header h1 {
            color: #333;
            font-size: 2.5em;
            margin-bottom: 10px;
        }
        
        .header p {
            color: #666;
            font-size: 1.1em;
        }
        
        .main-content {
            background: white;
            padding: 30px;
            border-radius: 0 0 10px 10px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        
        .search-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        
        .search-section h2 {
            color: #333;
            margin-bottom: 20px;
            font-size: 1.5em;
        }
        
        .form-row {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            margin-bottom: 15px;
        }
        
        .form-group {
            flex: 1;
            min-width: 200px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: 500;
        }
        
        .form-group input, 
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 1em;
            transition: border-color 0.3s;
        }
        
        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            cursor: pointer;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background: #6c757d;
        }
        
        .btn-secondary:hover {
            box-shadow: 0 5px 20px rgba(108, 117, 125, 0.4);
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .room-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        
        .room-card {
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            transition: all 0.3s;
            cursor: pointer;
        }
        
        .room-card:hover {
            border-color: #667eea;
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        
        .room-card.selected {
            border-color: #28a745;
            background: #f0fff4;
        }
        
        .room-number {
            font-size: 1.3em;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }
        
        .room-type {
            color: #667eea;
            font-weight: 500;
            margin-bottom: 10px;
        }
        
        .room-price {
            font-size: 1.2em;
            color: #28a745;
            font-weight: bold;
            margin-bottom: 10px;
        }
        
        .room-status {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 0.9em;
            font-weight: 500;
        }
        
        .status-available {
            background: #d4edda;
            color: #155724;
        }
        
        .hidden {
            display: none;
        }
        
        .progress-bar {
            display: flex;
            justify-content: space-between;
            margin-bottom: 30px;
            position: relative;
        }
        
        .progress-bar::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 2px;
            background: #e0e0e0;
            transform: translateY(-50%);
            z-index: 1;
        }
        
        .progress-step {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: white;
            border: 2px solid #e0e0e0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            position: relative;
            z-index: 2;
        }
        
        .progress-step.active {
            border-color: #667eea;
            background: #667eea;
            color: white;
        }
        
        .progress-step.completed {
            border-color: #28a745;
            background: #28a745;
            color: white;
        }
        
        .step-content {
            display: none;
        }
        
        .step-content.active {
            display: block;
        }
        
        .booking-summary {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
        }
        
        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #dee2e6;
        }
        
        .summary-row:last-child {
            border-bottom: none;
        }
        
        .summary-label {
            font-weight: 600;
            color: #495057;
        }
        
        .summary-value {
            color: #212529;
        }
        
        .total-amount {
            font-size: 1.2em;
            color: #28a745;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🏨 Hotel Reservation System</h1>
            <p>Book your perfect stay with us</p>
        </div>
        
        <div class="main-content">
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("success") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("success") %>
                </div>
            <% } %>
            
            <!-- Progress Bar -->
            <div class="progress-bar">
                <div class="progress-step active" id="step1">1</div>
                <div class="progress-step" id="step2">2</div>
                <div class="progress-step" id="step3">3</div>
            </div>
            
            <!-- Step 1: Search Availability -->
            <div class="step-content active" id="step1-content">
                <div class="search-section">
                    <h2>🔍 Check Room Availability</h2>
                    <form action="<%= request.getContextPath() %>/addReservation" method="get">
                        <input type="hidden" name="action" value="checkAvailability">
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label>Check-in Date</label>
                                <input type="date" name="checkIn" required 
                                       min="<%= java.time.LocalDate.now() %>"
                                       value="<%= request.getAttribute("checkIn") != null ? request.getAttribute("checkIn") : "" %>">
                            </div>
                            
                            <div class="form-group">
                                <label>Check-out Date</label>
                                <input type="date" name="checkOut" required 
                                       min="<%= java.time.LocalDate.now().plusDays(1) %>"
                                       value="<%= request.getAttribute("checkOut") != null ? request.getAttribute("checkOut") : "" %>">
                            </div>
                            
                            <div class="form-group">
                                <label>Room Category</label>
                                <select name="category" required>
                                    <option value="">Select Category</option>
                                    <option value="Standard" <%= "Standard".equals(request.getAttribute("category")) ? "selected" : "" %>>Standard</option>
                                    <option value="Deluxe" <%= "Deluxe".equals(request.getAttribute("category")) ? "selected" : "" %>>Deluxe</option>
                                    <option value="Suite" <%= "Suite".equals(request.getAttribute("category")) ? "selected" : "" %>>Suite</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label>&nbsp;</label>
                                <button type="submit" class="btn">Check Availability</button>
                            </div>
                        </div>
                    </form>
                </div>
                
                <% if (request.getAttribute("availableRooms") != null) { %>
                    <% List<Room> availableRooms = (List<Room>) request.getAttribute("availableRooms"); %>
                    <% if (!availableRooms.isEmpty()) { %>
                        <h3>Available Rooms (<%= availableRooms.size() %> found)</h3>
                        <div class="room-grid">
                            <% for (Room room : availableRooms) { %>
                                <div class="room-card" onclick="selectRoom(this, <%= room.getRoomId() %>, '<%= room.getPrice() %>')">
                                    <div class="room-number">Room <%= room.getRoomNumber() %></div>
                                    <div class="room-type"><%= room.getCategoryName() != null ? room.getCategoryName() : "" %> - <%= room.getTypeName() != null ? room.getTypeName() : "" %></div>
                                    <div class="room-price">LKR <%= room.getPrice() != null ? room.getPrice() : "0.00" %> per night</div>
                                    <span class="room-status status-available">Available</span>
                                </div>
                            <% } %>
                        </div>
                        
                        <div style="margin-top: 20px; text-align: right;">
                            <button class="btn" onclick="nextStep(2)" id="nextToGuestBtn" disabled>Next: Guest Details</button>
                        </div>
                    <% } else { %>
                        <div class="alert alert-error">No rooms available for selected dates and category.</div>
                    <% } %>
                <% } %>
            </div>
            
            <!-- Step 2: Guest Details -->
            <div class="step-content" id="step2-content">
                <h2>👤 Guest Information</h2>
                <form id="guestForm" onsubmit="return false;">
                    <div class="form-row">
                        <div class="form-group">
                            <label>First Name *</label>
                            <input type="text" id="firstName" required pattern="[A-Za-z ]+" 
                                   title="Only letters and spaces allowed">
                        </div>
                        
                        <div class="form-group">
                            <label>Last Name *</label>
                            <input type="text" id="lastName" required pattern="[A-Za-z ]+"
                                   title="Only letters and spaces allowed">
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Address *</label>
                            <textarea id="address" rows="3" required></textarea>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Contact Number *</label>
                            <input type="tel" id="contact" required pattern="[0-9]{10}" 
                                   placeholder="10-digit mobile number" maxlength="10">
                            <small>Format: 10 digits only</small>
                        </div>
                        
                        <div class="form-group">
                            <label>Email *</label>
                            <input type="email" id="email" required placeholder="guest@example.com">
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Number of Adults *</label>
                            <input type="number" id="adults" min="1" max="4" value="1" required>
                        </div>
                        
                        <div class="form-group">
                            <label>Number of Children</label>
                            <input type="number" id="children" min="0" max="3" value="0">
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Special Requests</label>
                            <textarea id="specialRequests" rows="3" placeholder="Any special requirements..."></textarea>
                        </div>
                    </div>
                    
                    <div style="display: flex; gap: 10px; justify-content: flex-end;">
                        <button type="button" class="btn btn-secondary" onclick="prevStep(1)">Back</button>
                        <button type="button" class="btn" onclick="nextStep(3)">Next: Review & Confirm</button>
                    </div>
                </form>
            </div>
            
            <!-- Step 3: Review & Confirm -->
            <div class="step-content" id="step3-content">
                <h2>📋 Review Your Booking</h2>
                
                <div class="booking-summary">
                    <h3>Room Details</h3>
                    <div id="reviewRoomDetails" class="summary-row">
                        <span class="summary-label">Selected Room:</span>
                        <span class="summary-value" id="reviewRoomValue"></span>
                    </div>
                    <div class="summary-row">
                        <span class="summary-label">Price per Night:</span>
                        <span class="summary-value" id="reviewPriceValue"></span>
                    </div>
                    
                    <h3 style="margin-top: 20px;">Guest Details</h3>
                    <div id="reviewGuestDetails"></div>
                    
                    <h3 style="margin-top: 20px;">Booking Summary</h3>
                    <div id="reviewBookingSummary"></div>
                    
                    <div class="summary-row total-amount">
                        <span class="summary-label">Total Amount:</span>
                        <span class="summary-value" id="totalAmountValue"></span>
                    </div>
                </div>
                
                <div style="display: flex; gap: 10px; justify-content: flex-end;">
                    <button type="button" class="btn btn-secondary" onclick="prevStep(2)">Back</button>
                    <button type="button" class="btn" onclick="submitReservation()">Confirm Booking</button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Hidden form for submission -->
    <form id="roomSelectionForm" action="<%= request.getContextPath() %>/addReservation" method="post" style="display: none;">
        <input type="hidden" name="action" value="createReservation">
        <input type="hidden" name="roomId" id="selectedRoomId">
        <input type="hidden" name="checkIn" id="selectedCheckIn" value="<%= request.getAttribute("checkIn") != null ? request.getAttribute("checkIn") : "" %>">
        <input type="hidden" name="checkOut" id="selectedCheckOut" value="<%= request.getAttribute("checkOut") != null ? request.getAttribute("checkOut") : "" %>">
        <input type="hidden" name="firstName" id="hiddenFirstName">
        <input type="hidden" name="lastName" id="hiddenLastName">
        <input type="hidden" name="address" id="hiddenAddress">
        <input type="hidden" name="contact" id="hiddenContact">
        <input type="hidden" name="email" id="hiddenEmail">
        <input type="hidden" name="adults" id="hiddenAdults">
        <input type="hidden" name="children" id="hiddenChildren">
        <input type="hidden" name="specialRequests" id="hiddenSpecialRequests">
    </form>
    
    <script>
        let selectedRoomId = null;
        let selectedRoomCard = null;
        let selectedRoomPrice = 0;
        
        function selectRoom(element, roomId, price) {
            if (selectedRoomCard) {
                selectedRoomCard.classList.remove('selected');
            }
            element.classList.add('selected');
            selectedRoomCard = element;
            selectedRoomId = roomId;
            selectedRoomPrice = parseFloat(price);
            document.getElementById('nextToGuestBtn').disabled = false;
        }
        
        function nextStep(step) {
            if (step === 2) {
                if (!selectedRoomId) {
                    alert('Please select a room first');
                    return;
                }
                
                // Store selected dates in hidden fields
                const checkIn = document.querySelector('input[name="checkIn"]').value;
                const checkOut = document.querySelector('input[name="checkOut"]').value;
                document.getElementById('selectedCheckIn').value = checkIn;
                document.getElementById('selectedCheckOut').value = checkOut;
                
                document.getElementById('step1-content').classList.remove('active');
                document.getElementById('step2-content').classList.add('active');
                
                document.getElementById('step1').classList.remove('active');
                document.getElementById('step1').classList.add('completed');
                document.getElementById('step2').classList.add('active');
            } else if (step === 3) {
                // Validate guest form
                const firstName = document.getElementById('firstName').value.trim();
                const lastName = document.getElementById('lastName').value.trim();
                const address = document.getElementById('address').value.trim();
                const contact = document.getElementById('contact').value.trim();
                const email = document.getElementById('email').value.trim();
                const adults = document.getElementById('adults').value;
                
                if (!firstName || !lastName || !address || !contact || !email) {
                    alert('Please fill in all required fields');
                    return;
                }
                
                if (!/^[0-9]{10}$/.test(contact)) {
                    alert('Please enter a valid 10-digit contact number');
                    return;
                }
                
                if (!/^\S+@\S+\.\S+$/.test(email)) {
                    alert('Please enter a valid email address');
                    return;
                }
                
                if (parseInt(adults) < 1) {
                    alert('At least 1 adult is required');
                    return;
                }
                
                // Store guest data in hidden fields
                document.getElementById('hiddenFirstName').value = firstName;
                document.getElementById('hiddenLastName').value = lastName;
                document.getElementById('hiddenAddress').value = address;
                document.getElementById('hiddenContact').value = contact;
                document.getElementById('hiddenEmail').value = email;
                document.getElementById('hiddenAdults').value = adults;
                document.getElementById('hiddenChildren').value = document.getElementById('children').value;
                document.getElementById('hiddenSpecialRequests').value = document.getElementById('specialRequests').value;
                
                // Update review section
                updateReview();
                
                document.getElementById('step2-content').classList.remove('active');
                document.getElementById('step3-content').classList.add('active');
                
                document.getElementById('step2').classList.remove('active');
                document.getElementById('step2').classList.add('completed');
                document.getElementById('step3').classList.add('active');
            }
        }
        
        function prevStep(step) {
            if (step === 1) {
                document.getElementById('step2-content').classList.remove('active');
                document.getElementById('step1-content').classList.add('active');
                
                document.getElementById('step2').classList.remove('active', 'completed');
                document.getElementById('step1').classList.remove('completed');
                document.getElementById('step1').classList.add('active');
            } else if (step === 2) {
                document.getElementById('step3-content').classList.remove('active');
                document.getElementById('step2-content').classList.add('active');
                
                document.getElementById('step3').classList.remove('active');
                document.getElementById('step2').classList.remove('completed');
                document.getElementById('step2').classList.add('active');
            }
        }
        
        function updateReview() {
            // Room details
            const roomCard = selectedRoomCard.querySelector('.room-number').textContent;
            const roomType = selectedRoomCard.querySelector('.room-type').textContent;
            
            document.getElementById('reviewRoomValue').innerHTML = `${roomCard} - ${roomType}`;
            document.getElementById('reviewPriceValue').innerHTML = `LKR ${selectedRoomPrice.toFixed(2)}`;
            
            // Guest details
            const firstName = document.getElementById('firstName').value;
            const lastName = document.getElementById('lastName').value;
            const contact = document.getElementById('contact').value;
            const email = document.getElementById('email').value;
            const address = document.getElementById('address').value;
            
            document.getElementById('reviewGuestDetails').innerHTML = `
                <div class="summary-row">
                    <span class="summary-label">Name:</span>
                    <span class="summary-value">${firstName} ${lastName}</span>
                </div>
                <div class="summary-row">
                    <span class="summary-label">Contact:</span>
                    <span class="summary-value">${contact}</span>
                </div>
                <div class="summary-row">
                    <span class="summary-label">Email:</span>
                    <span class="summary-value">${email}</span>
                </div>
                <div class="summary-row">
                    <span class="summary-label">Address:</span>
                    <span class="summary-value">${address}</span>
                </div>
            `;
            
            // Booking summary
            const checkIn = document.getElementById('selectedCheckIn').value;
            const checkOut = document.getElementById('selectedCheckOut').value;
            const adults = document.getElementById('adults').value;
            const children = document.getElementById('children').value;
            
            // Calculate nights
            const start = new Date(checkIn);
            const end = new Date(checkOut);
            const nights = Math.round((end - start) / (1000 * 60 * 60 * 24));
            
            // Calculate total
            const totalAmount = selectedRoomPrice * nights;
            
            document.getElementById('reviewBookingSummary').innerHTML = `
                <div class="summary-row">
                    <span class="summary-label">Check-in:</span>
                    <span class="summary-value">${checkIn}</span>
                </div>
                <div class="summary-row">
                    <span class="summary-label">Check-out:</span>
                    <span class="summary-value">${checkOut}</span>
                </div>
                <div class="summary-row">
                    <span class="summary-label">Nights:</span>
                    <span class="summary-value">${nights}</span>
                </div>
                <div class="summary-row">
                    <span class="summary-label">Guests:</span>
                    <span class="summary-value">${adults} Adults, ${children} Children</span>
                </div>
                <div class="summary-row">
                    <span class="summary-label">Special Requests:</span>
                    <span class="summary-value">${document.getElementById('specialRequests').value || 'None'}</span>
                </div>
            `;
            
            document.getElementById('totalAmountValue').innerHTML = `LKR ${totalAmount.toFixed(2)}`;
        }
        
        function submitReservation() {
            // Validate all data before submission
            if (!selectedRoomId) {
                alert('Please select a room');
                return;
            }
            
            const form = document.getElementById('roomSelectionForm');
            document.getElementById('selectedRoomId').value = selectedRoomId;
            
            // Form is already populated with hidden fields
            form.submit();
        }
    </script>
</body>
</html>