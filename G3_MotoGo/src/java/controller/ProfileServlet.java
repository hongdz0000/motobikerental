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
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author MSI LAPTOP
 */
public class ProfileServlet extends HttpServlet {
   
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
            out.println("<title>Servlet ProfileServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileServlet at " + request.getContextPath () + "</h1>");
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
          // 1. Kiểm tra Session xem user đã đăng nhập chưa
    HttpSession session = request.getSession();
    User currentUser = (User) session.getAttribute("user");

    if (currentUser == null) {
        // Nếu chưa đăng nhập, chuyển hướng thẳng về trang Login
        response.sendRedirect(request.getContextPath() + "/templates/login");
        return; // Kết thúc hàm để không chạy code phía dưới
    }

    // 2. Lấy userID từ tham số URL (đã truyền từ JSP sang)
    String userId = request.getParameter("id");
    
    UserDAO accdal = new UserDAO();
    User acc;

    if (userId != null && !userId.isEmpty()) {
        // Nếu có truyền ID trên URL, lấy dữ liệu theo ID đó
        acc = accdal.getAccountByUserId(userId);
    } else {
        // Nếu không truyền ID, mặc định lấy thông tin của chính người đang đăng nhập
        acc = currentUser;
    }

    // 3. Gửi dữ liệu user sang trang JSP để hiển thị
    if (acc != null) {
        request.setAttribute("profileUser", acc);
        request.getRequestDispatcher("templates/user-profile.jsp").forward(request, response);
    } else {
        // Nếu không tìm thấy user theo ID, có thể báo lỗi hoặc về trang chủ
        response.sendRedirect(request.getContextPath() + "/templates/index.jsp");
    }
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
         String userId = request.getParameter("userId");
    String newName = request.getParameter("userName");
    String newPhone = request.getParameter("phone");

    // 2. Gọi DAO để cập nhật vào Database
    UserDAO dao = new UserDAO();
    boolean isUpdated = dao.updateUserProfile(userId, newName, newPhone);

    if (isUpdated) {
        // Cập nhật lại session nếu người dùng đang sửa profile của chính mình
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser != null && String.valueOf(currentUser.getUserId()).equals(userId)) {
            currentUser.setUserName(newName);
            currentUser.setPhone(newPhone);
            request.getSession().setAttribute("user", currentUser);
        }
        
        // 3. Quay lại trang Profile để xem thay đổi (gọi lại doGet)
        response.sendRedirect(request.getContextPath() + "/Profile?id=" + userId);
    } else {
        // Xử lý khi lỗi (ví dụ báo lỗi ra màn hình)
        request.setAttribute("err", "Update failed!");
        request.getRequestDispatcher("templates/user-profile.jsp").forward(request, response);
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
