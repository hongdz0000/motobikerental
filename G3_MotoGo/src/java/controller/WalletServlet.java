/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.TransactionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.Transaction;
import model.User;

/**
 *
 * @author thais
 */
public class WalletServlet extends HttpServlet {

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
            out.println("<title>Servlet WalletServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet WalletServlet at " + request.getContextPath() + "</h1>");
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

        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/templates/login.jsp");
            return;
        }

        int userId = currentUser.getUserId();

        TransactionDAO dao = new TransactionDAO();

        int page = 1;
        int pageSize = 10;
        dao.syncUserBalance(userId);

        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        int totalTransactions = dao.countTransactionsByUser(userId);
        int totalPages = (int) Math.ceil((double) totalTransactions / pageSize);

        ArrayList<Transaction> transactions
                = dao.getTransactionsByUserPaging(userId, page, pageSize);

        double balance = dao.getWalletBalance(userId);

        request.setAttribute("transactions", transactions);
        request.setAttribute("balance", balance);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/templates/wallet.jsp").forward(request, response);
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
  HttpSession session = request.getSession();
    User currentUser = (User) session.getAttribute("user");

    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "templates/login.jsp");
        return;
    }

    String amountRaw = request.getParameter("amount");
    if (amountRaw != null && !amountRaw.isEmpty()) {
        try {
            double amount = Double.parseDouble(amountRaw);
            if (amount > 0) {
                TransactionDAO dao = new TransactionDAO();
                dao.depositMoney(currentUser.getUserId(), amount);

                // --- CẬP NHẬT SESSION TẠI ĐÂY ---
                double newBalance = dao.getWalletBalance(currentUser.getUserId());
                currentUser.setBalance(BigDecimal.valueOf(newBalance)); 
                session.setAttribute("user", currentUser); 
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    response.sendRedirect(request.getContextPath() + "/wallet");
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
