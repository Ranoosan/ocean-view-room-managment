package controller;

import model.ReservationDAO;
import model.Bill;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/checkOut")
public class CheckOutServlet extends HttpServlet {
    
    private ReservationDAO reservationDAO = new ReservationDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int reservationId = Integer.parseInt(request.getParameter("reservationId"));
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String checkOutDateStr = request.getParameter("checkOutDate");
            
            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr, DateTimeFormatter.ISO_DATE);
            
            Bill bill = reservationDAO.checkOut(reservationId, roomId, checkOutDate);
            
            request.setAttribute("bill", bill);
            request.setAttribute("success", "Check-out completed successfully!");
            
        } catch (Exception e) {
            request.setAttribute("error", "Error during check-out: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/billDetails.jsp").forward(request, response);
    }
}