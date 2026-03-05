package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDAO;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User(username, password);

        User validUser = UserDAO.validate(user);

        if (validUser != null) {
            // create session
            HttpSession session = request.getSession();
            session.setAttribute("username", validUser.getUsername());
            session.setAttribute("role", validUser.getRole());

            // redirect to dashboard
            response.sendRedirect("dashboard.jsp");

        } else {
            request.setAttribute("errorMessage", "Invalid Username or Password!");
            request.getRequestDispatcher("login.jsp")
                   .forward(request, response);
        }
    }

    // Optional: handle GET requests (redirect to login page)
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}