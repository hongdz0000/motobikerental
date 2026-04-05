/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.MotorbikeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Motorbike;
import model.User;

/**
 *
 * @author MSI LAPTOP
 */
public class BikeListServlet extends HttpServlet {

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
            out.println("<title>Servlet BikeListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BikeListServlet at " + request.getContextPath() + "</h1>");
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

        MotorbikeDAO dao = new MotorbikeDAO();

        int page = 1;
        int pageSize = 8;

        // sorting
        String sort = request.getParameter("sort");
        if (sort == null || sort.isEmpty()) {
            sort = "default";
        }

        // filters
        String brand = request.getParameter("brand");
        String engine = request.getParameter("engineSize");
        String transmission = request.getParameter("transmission");

        // pagination
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        int offset = (page - 1) * pageSize;

        // get filtered bikes
        List<Motorbike> list = dao.getMotorbikesFiltered(
                brand,
                engine,
                transmission,
                sort,
                offset,
                pageSize
        );

        // count bikes for pagination
        int totalBikes = dao.getTotalMotorbikesFiltered(
                brand,
                engine,
                transmission
        );

        int totalPages = (int) Math.ceil((double) totalBikes / pageSize);

        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        // brand dropdown
        List<String> brands = dao.getAllBrands();

        // send data to JSP
        request.setAttribute("brands", brands);
        request.setAttribute("bikeList", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // preserve selected filters in UI
        request.setAttribute("selectedBrand", brand);
        request.setAttribute("selectedEngine", engine);
        request.setAttribute("selectedTransmission", transmission);
        request.setAttribute("selectedSort", sort);

        request.getRequestDispatcher("/templates/bike-list.jsp")
                .forward(request, response);
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
