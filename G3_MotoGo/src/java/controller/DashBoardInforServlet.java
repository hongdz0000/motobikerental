/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.MotorbikeDAO;
import dal.RentOrderDAO;
import dal.StuffDAO;
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
public class DashBoardInforServlet extends HttpServlet {
   
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
            out.println("<title>Servlet DashBoardInforServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashBoardInforServlet at " + request.getContextPath () + "</h1>");
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
        HttpSession session = request.getSession();
    User currentUser = (User) session.getAttribute("user");

    if (currentUser == null) {
        // Nếu chưa đăng nhập, chuyển hướng thẳng về trang Login
        response.sendRedirect(request.getContextPath() + "/templates/login.jsp");
        return; // Kết thúc hàm để không chạy code phía dưới
    }
       // Khởi tạo các lớp DAO
    UserDAO uDao = new UserDAO();
    RentOrderDAO rDao = new RentOrderDAO();
    MotorbikeDAO mDao = new MotorbikeDAO();
        StuffDAO sDao = new StuffDAO();

    // Lấy dữ liệu từ database thông qua DAO
    int totalUsers = uDao.countUsersByRole("user");
    int totalStaff = uDao.countUsersByRole("admin"); // Hoặc 'admin' tùy logic của bạn
    int totalOrders = rDao.getTotalOrders();
    int pendingOrders = rDao.getPendingOrdersCount();
    double totalRevenue = rDao.getTotalRevenue();
    int totalBikes = mDao.getTotalBikes();
      int totalStuff = sDao.getTotalStuffCount();

    // Set các thuộc tính vào request (tên biến phải khớp với EL ${...} trong JSP)
    request.setAttribute("totalUsers", totalUsers);
    request.setAttribute("totalOrders", totalOrders);
    request.setAttribute("totalRevenue", String.format("%,.0f", totalRevenue)); // Định dạng số có dấu phẩy
    request.setAttribute("pendingOrders", pendingOrders);
    request.setAttribute("totalBikes", totalBikes);
    request.setAttribute("totalStaff", totalStaff);
    request.setAttribute("totalStuff", totalStuff);
    // Chuyển hướng đến trang JSP dashboard
    request.getRequestDispatcher("/templates/admin/dashboard.jsp").forward(request, response);

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
        processRequest(request, response);
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
