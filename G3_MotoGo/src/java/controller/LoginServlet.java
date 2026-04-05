/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

/**
 *
 * @author MSI LAPTOP
 */
public class LoginServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet LoginServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            // 1. Lấy dữ liệu từ form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

// 2. Gọi lớp DAO để kiểm tra
        UserDAO accdal = new UserDAO();
        User acc = accdal.getAccountByEmail(email);

// 3. Kiểm tra logic đăng nhập
        if (acc != null && acc.getUserPassword().equals(password)) {
            // Đăng nhập thành công: Lưu vào Session
            request.getSession().setAttribute("user", acc);

            // KIỂM TRA ROLE ĐỂ ĐIỀU HƯỚNG
            if ("admin".equalsIgnoreCase(acc.getRole())) {
                // Nếu là admin -> qua trang dashboard admin
                response.sendRedirect(request.getContextPath() + "/DashBoardInfor");
            } else {
                // Nếu là user (hoặc các role khác) -> sang index như cũ
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } else {
            // Đăng nhập thất bại
            request.setAttribute("err", "Tài khoản hoặc mật khẩu không chính xác!");
            request.getRequestDispatcher("/templates/login.jsp").forward(request, response);
        }

    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
