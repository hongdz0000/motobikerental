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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Motorbike;
import model.Stuff;

/**
 *
 * @author thais
 */
public class BikeStuffServlet extends HttpServlet {

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
            out.println("<title>Servlet BikeStuffServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BikeStuffServlet at " + request.getContextPath() + "</h1>");
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
        String bikeIdRaw = request.getParameter("bikeId");
        if (bikeIdRaw == null || bikeIdRaw.isEmpty()) {
            response.sendRedirect("AdminBike");
            return;
        }

        int bikeId = Integer.parseInt(bikeIdRaw);
        String txtSearch = request.getParameter("txtSearch");
        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        int pageSize = 5;

        StuffDAO stuffDao = new StuffDAO();
        MotorbikeDAO bikeDao = new MotorbikeDAO();

        // 1. Lấy thông tin xe
        Motorbike selectedBike = bikeDao.getMotorbikeById(bikeId);
        
        // 2. Lấy danh sách phụ kiện ĐÃ GÁN (Phân trang + Search)
        Map<Stuff, Integer> stuffMap = stuffDao.getStuffMapByBikePaging(bikeId, txtSearch, page, pageSize);
        int totalRecords = stuffDao.countStuffByBike(bikeId, txtSearch);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // 3. Lấy danh sách phụ kiện CHƯA GÁN (Để hiện trong Pop-up)
        List<Stuff> availableToAdd = stuffDao.getAvailableStuffToAddToBike(bikeId, null);

        request.setAttribute("selectedBike", selectedBike);
        request.setAttribute("stuffMap", stuffMap);
        request.setAttribute("availableToAdd", availableToAdd); // Danh sách cho Pop-up
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("txtSearch", txtSearch);
        request.setAttribute("selectedBikeId", bikeId);

        request.getRequestDispatcher("/templates/admin/bike-stuff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String bikeIdRaw = request.getParameter("bikeId");
        int bikeId = Integer.parseInt(bikeIdRaw);
        StuffDAO dao = new StuffDAO();

        try {
            if ("save".equals(action)) {
                int stuffId = Integer.parseInt(request.getParameter("stuffId"));
                String overridePriceStr = request.getParameter("overridePrice");
                Double overridePrice = (overridePriceStr != null && !overridePriceStr.isEmpty()) 
                                       ? Double.parseDouble(overridePriceStr) : null;
                int qty = Integer.parseInt(request.getParameter("includedQty"));
                
                dao.updateBikeStuffConfig(bikeId, stuffId, overridePrice, qty);

            } else if ("add".equals(action)) {
                // Thêm mới cấu hình (Mặc định qty=1, price=null)
                int stuffId = Integer.parseInt(request.getParameter("stuffId"));
                dao.addNewBikeStuffConfig(bikeId, stuffId);

            } else if ("delete".equals(action)) {
                // Chỉ xóa liên kết trong config, không xóa stuff gốc
                int stuffId = Integer.parseInt(request.getParameter("stuffId"));
                dao.deleteBikeStuffConfig(bikeId, stuffId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        response.sendRedirect("accessories?bikeId=" + bikeId);
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
