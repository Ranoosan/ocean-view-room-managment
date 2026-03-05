package controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import model.ReservationDAO;
import model.Reservation;

public class AddReservationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        Reservation r = new Reservation();

        String reservationNo = "R" + (1000 + (int)(Math.random() * 9000));

        r.setReservationNo(reservationNo);
        r.setGuestName(request.getParameter("guestName"));
        r.setNic(request.getParameter("nic"));
        r.setAddress(request.getParameter("address"));
        r.setContactNumber(request.getParameter("contact"));
        r.setRoomCategory(request.getParameter("roomCategory"));
        r.setRoomType(request.getParameter("roomType"));
        r.setCheckIn(request.getParameter("checkin"));
        r.setCheckOut(request.getParameter("checkout"));

        boolean status = ReservationDAO.addReservation(r);

        if(status) {
            request.setAttribute("reservationNo", reservationNo);
            request.getRequestDispatcher("reservationSuccess.jsp")
                    .forward(request,response);
        }
        else {
            response.sendRedirect("addReservation.jsp");
        }
    }
}