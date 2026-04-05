/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.OrderDAO;
import dal.RentOrderDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Order;
import model.RentOrder;
import model.User;

/**
 *
 * @author MSI LAPTOP
 */
public class UpcomingRentalServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UpcomingRentalServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpcomingRentalServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("templates/login.jsp");
            return;
        }

        OrderDAO dao = new OrderDAO();
        String action = request.getParameter("action");
        int userId = user.getUserId();

        // 1. Handle CANCEL with Refund
        if ("cancel".equals(action)) {
            int orderId = Integer.parseInt(request.getParameter("id"));
            // This method calculates refund based on 'pending' or 'confirmed'
            String result = dao.processCancelAndRefund(userId, orderId);

            if (result.contains("successfully")) {
                refreshUserSession(session, userId);
                request.setAttribute("msg", result);
            } else {
                request.setAttribute("error", result);
            }
        }

        // 2. Handle FINAL PAYMENT (Total - Deposit)
        if ("pay".equals(action)) {
            int orderId = Integer.parseInt(request.getParameter("id"));
            String result = dao.processPayment(userId, orderId);

            if ("success".equals(result)) {
                refreshUserSession(session, userId);
                request.setAttribute("msg", "Payment successful! Your trip is now active.");
            } else {
                request.setAttribute("error", result);
            }
        }

        // 3. Search, Sort & Pagination
        String txtSearch = request.getParameter("search") == null ? "" : request.getParameter("search");
        String sort = request.getParameter("sort") == null ? "SOONEST" : request.getParameter("sort");
        String indexPage = request.getParameter("page");
        int index = (indexPage == null) ? 1 : Integer.parseInt(indexPage);

        int totalOrders = dao.getTotalUpcoming(userId, txtSearch);
        int endPage = (int) Math.ceil((double) totalOrders / 5);
        List<Order> list = dao.getUpcomingOrders(userId, sort, txtSearch, index);

        // Send data to JSP
        request.setAttribute("orders", list);
        request.setAttribute("endP", endPage);
        request.setAttribute("tag", index);
        request.setAttribute("search", txtSearch);
        request.setAttribute("sort", sort);
        request.setAttribute("total", totalOrders);

        request.getRequestDispatcher("templates/upcoming-rentals.jsp").forward(request, response);
    }

    private void refreshUserSession(HttpSession session, int userId) {
        UserDAO uDao = new UserDAO();
        // Matching your method name 'getAccountByUserId'
        session.setAttribute("user", uDao.getAccountByUserId(Integer.toString(userId)));
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
