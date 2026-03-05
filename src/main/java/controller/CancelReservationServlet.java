package controller;

import model.ReservationDAO;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class CancelReservationServlet extends HttpServlet {
    
    private ReservationDAO reservationDAO = new ReservationDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String reservationIdStr = request.getParameter("reservationId");
        
        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            boolean cancelled = reservationDAO.cancelReservation(reservationId);
            
            if (cancelled) {
                request.getSession().setAttribute("message", "Reservation cancelled successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to cancel reservation");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error cancelling reservation: " + e.getMessage());
        }
        
        response.sendRedirect("viewReservations");
    }
}