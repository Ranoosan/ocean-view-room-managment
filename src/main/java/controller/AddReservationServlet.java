package controller;

import model.ReservationDAO;
import model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/addReservation")
public class AddReservationServlet extends HttpServlet {
    
    private ReservationDAO reservationDAO = new ReservationDAO();
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("checkAvailability".equals(action)) {
            try {
                String checkInStr = request.getParameter("checkIn");
                String checkOutStr = request.getParameter("checkOut");
                String category = request.getParameter("category");
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate checkIn = LocalDate.parse(checkInStr, formatter);
                LocalDate checkOut = LocalDate.parse(checkOutStr, formatter);
                
                // Validate dates
                if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                    request.setAttribute("error", "Check-out date must be after check-in date");
                    request.getRequestDispatcher("/reservationForm.jsp").forward(request, response);
                    return;
                }
                
                // Get available rooms
                List<Room> availableRooms = reservationDAO.getAvailableRooms(checkIn, checkOut, category);
                request.setAttribute("availableRooms", availableRooms);
                request.setAttribute("checkIn", checkInStr);
                request.setAttribute("checkOut", checkOutStr);
                request.setAttribute("category", category);
                
            } catch (DateTimeParseException e) {
                request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD");
            } catch (Exception e) {
                request.setAttribute("error", "Error checking availability: " + e.getMessage());
            }
        }
        
        request.getRequestDispatcher("/reservationForm.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("createReservation".equals(action)) {
            try {
                // Get form data
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String address = request.getParameter("address");
                String contact = request.getParameter("contact");
                String email = request.getParameter("email");
                String checkInStr = request.getParameter("checkIn");
                String checkOutStr = request.getParameter("checkOut");
                String roomIdStr = request.getParameter("roomId");
                String adultsStr = request.getParameter("adults");
                String childrenStr = request.getParameter("children");
                String specialRequests = request.getParameter("specialRequests");
                
                // Validate input
                StringBuilder errors = new StringBuilder();
                
                // Validate required fields
                if (firstName == null || firstName.trim().isEmpty()) {
                    errors.append("First name is required.<br>");
                }
                if (lastName == null || lastName.trim().isEmpty()) {
                    errors.append("Last name is required.<br>");
                }
                if (address == null || address.trim().isEmpty()) {
                    errors.append("Address is required.<br>");
                }
                
                // Validate contact number
                if (contact == null || !PHONE_PATTERN.matcher(contact).matches()) {
                    errors.append("Invalid contact number. Must be 10 digits.<br>");
                }
                
                // Validate email
                if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    errors.append("Invalid email address.<br>");
                }
                
                // Validate dates
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate checkIn = LocalDate.parse(checkInStr, formatter);
                LocalDate checkOut = LocalDate.parse(checkOutStr, formatter);
                
                if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                    errors.append("Check-out date must be after check-in date.<br>");
                }
                
                // Validate room selection
                int roomId = Integer.parseInt(roomIdStr);
                if (!reservationDAO.isRoomAvailable(roomId, checkIn, checkOut)) {
                    errors.append("Selected room is no longer available for these dates.<br>");
                }
                
                if (errors.length() > 0) {
                    request.setAttribute("error", errors.toString());
                    request.getRequestDispatcher("/reservationForm.jsp").forward(request, response);
                    return;
                }
                
                // Create guest object
                Guest guest = new Guest();
                guest.setFirstName(firstName.trim());
                guest.setLastName(lastName.trim());
                guest.setAddress(address.trim());
                guest.setContactNumber(contact.trim());
                guest.setEmail(email.trim());
                
                // Create reservation object
                Reservation reservation = new Reservation();
                reservation.setExpectedCheckIn(checkIn);
                reservation.setExpectedCheckOut(checkOut);
                reservation.setAdultsCount(Integer.parseInt(adultsStr));
                reservation.setChildrenCount(childrenStr != null && !childrenStr.isEmpty() ? 
                                           Integer.parseInt(childrenStr) : 0);
                reservation.setSpecialRequests(specialRequests);
                
                // Save reservation
                Reservation createdReservation = reservationDAO.createReservation(reservation, guest, roomId);
                
                request.setAttribute("success", "Reservation created successfully!");
                request.setAttribute("reservationNumber", createdReservation.getReservationNumber());
                request.getRequestDispatcher("/reservationConfirmation.jsp").forward(request, response);
                
            } catch (DateTimeParseException e) {
                request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD");
                request.getRequestDispatcher("/reservationForm.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid number format");
                request.getRequestDispatcher("/reservationForm.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error creating reservation: " + e.getMessage());
                request.getRequestDispatcher("/reservationForm.jsp").forward(request, response);
            }
        }
    }
}