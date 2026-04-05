/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.MotorbikeDAO;
import dal.StuffDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import model.Motorbike;
import model.Order;
import model.Stuff;

/**
 *
 * @author MSI LAPTOP
 */
public class CheckoutServlet extends HttpServlet {
   
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
            out.println("<title>Servlet CheckoutServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutServlet at " + request.getContextPath () + "</h1>");
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
           try {
            int bikeId = Integer.parseInt(request.getParameter("bikeId"));
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String[] selectedStuffIds = request.getParameterValues("selectedStuff");

            // 1. Lấy thông tin xe
            Motorbike bike = new MotorbikeDAO().getMotorbikeById(bikeId);
            
            // 2. Lấy danh sách phụ kiện VỚI GIÁ ĐÃ CẤU HÌNH THEO XE NÀY
            StuffDAO sDao = new StuffDAO();
            List<Stuff> stuffConfigsForThisBike = sDao.getStuffByMotorbikeId(bikeId);

            // 3. Tính số ngày
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);
            int days = (int) ChronoUnit.DAYS.between(start, end);
            if (days <= 0) days = 1;

            // 4. Lọc ra những món khách chọn và tính tiền dựa trên giá "final_price"
            double totalStuffCost = 0;
            List<Stuff> selectedList = new ArrayList<>();
            
            if (selectedStuffIds != null) {
                for (String sId : selectedStuffIds) {
                    int id = Integer.parseInt(sId);
                    for (Stuff s : stuffConfigsForThisBike) {
                        if (s.getStuffId() == id) {
                            // Nếu giá > 0 thì mới tính tiền (vì có những món miễn phí - included)
                            totalStuffCost += s.getBasePricePerDay() * days;
                            selectedList.add(s);
                            break;
                        }
                    }
                }
            }

            // 5. Tạo Order tạm thời
            Order pendingOrder = new Order();
            pendingOrder.setBikeId(bikeId);
            pendingOrder.setBikeName(bike.getName());
            pendingOrder.setPricePerDayAtRent(bike.getCurrentPricePerDay());
            pendingOrder.setStartDate(startDateStr);
            pendingOrder.setEndDate(endDateStr);
            pendingOrder.setTotalDays(days);
            pendingOrder.setTotalBikeCost(days * bike.getCurrentPricePerDay());
            pendingOrder.setTotalStuffCost(totalStuffCost);
            pendingOrder.setTotalAmount(pendingOrder.getTotalBikeCost() + totalStuffCost);
            pendingOrder.setDepositAmount(pendingOrder.getTotalAmount() * 0.15);

            // 6. Lưu Session
            HttpSession session = request.getSession();
            session.setAttribute("pendingOrder", pendingOrder);
            session.setAttribute("selectedStuffs", selectedList);
            
            request.getRequestDispatcher("templates/checkout.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("SQL Error: " + e.getMessage()); 
     try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CheckoutServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutServlet at " + e.getMessage() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
