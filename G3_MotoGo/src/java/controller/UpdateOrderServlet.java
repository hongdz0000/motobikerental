/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author MSI LAPTOP
 */
public class UpdateOrderServlet extends HttpServlet {
   
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
            out.println("<title>Servlet UpdateOrderServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateOrderServlet at " + request.getContextPath () + "</h1>");
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
          
        // 1. Lấy tham số từ URL (ví dụ: updateOrder?id=10&status=confirmed)
        String idRaw = request.getParameter("id");
        String newStatus = request.getParameter("status");
        
        OrderDAO dao = new OrderDAO();
        
        try {
            int id = Integer.parseInt(idRaw);
            
            // 2. Gọi DAO để cập nhật trạng thái
            boolean isUpdated = dao.updateOrderStatus(id, newStatus);
            
            if (isUpdated) {
                // Nếu hoàn tất đơn hàng, bạn có thể cập nhật luôn thanh toán thành 'paid'
                if ("completed".equals(newStatus)) {
                    dao.updatePaymentStatus(id, "paid");
                }
                
                // Gửi thông báo thành công (tùy chọn)
                request.getSession().setAttribute("message", "Cập nhật trạng thái thành công!");
            } else {
                request.getSession().setAttribute("error", "Cập nhật thất bại.");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        // 3. Chuyển hướng về trang danh sách (tránh bị lặp lại request khi F5)
        response.sendRedirect("AdminOrder"); 
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
