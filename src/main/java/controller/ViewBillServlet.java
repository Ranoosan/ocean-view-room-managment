package controller;

import model.BillDAO;
import model.Bill;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ViewBillServlet extends HttpServlet {
    
    private BillDAO billDAO = new BillDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String billIdStr = request.getParameter("billId");
        
        if (billIdStr != null && !billIdStr.isEmpty()) {
            try {
                int billId = Integer.parseInt(billIdStr);
                Bill bill = billDAO.getBillById(billId);
                
                if (bill != null) {
                    request.setAttribute("bill", bill);
                    request.getRequestDispatcher("/billDetails.jsp").forward(request, response);
                } else {
                    response.sendRedirect("bills");
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error loading bill details");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("bills");
        }
    }
}