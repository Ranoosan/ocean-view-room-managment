package controller;

import model.ReservationDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/checkIn")
public class CheckInServlet extends HttpServlet {
    
    private ReservationDAO reservationDAO = new ReservationDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int reservationId = Integer.parseInt(request.getParameter("reservationId"));
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            
            reservationDAO.checkIn(reservationId, roomId);
            
            request.setAttribute("success", "Guest checked in successfully!");
            
        } catch (Exception e) {
            request.setAttribute("error", "Error during check-in: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/viewReservations");
    }
}